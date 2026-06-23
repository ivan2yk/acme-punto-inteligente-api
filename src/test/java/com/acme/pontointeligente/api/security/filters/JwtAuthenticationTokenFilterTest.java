package com.acme.pontointeligente.api.security.filters;

import com.acme.pontointeligente.api.security.utils.JwtTokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class JwtAuthenticationTokenFilterTest {

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private JwtAuthenticationTokenFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String USERNAME = "user@acme.com";

    @BeforeEach
    public void setUp() {
        filter = new JwtAuthenticationTokenFilter();
        ReflectionTestUtils.setField(filter, "userDetailsService", userDetailsService);
        ReflectionTestUtils.setField(filter, "jwtTokenUtil", jwtTokenUtil);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testValidBearerToken_ShouldSetAuthentication() throws Exception {
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);

        UserDetails userDetails = new User(USERNAME, "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USUARIO")));

        given(jwtTokenUtil.getUsernameFromToken(VALID_TOKEN)).willReturn(USERNAME);
        given(jwtTokenUtil.tokenValido(VALID_TOKEN)).willReturn(true);
        given(userDetailsService.loadUserByUsername(USERNAME)).willReturn(userDetails);

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(USERNAME, SecurityContextHolder.getContext().getAuthentication().getName());
        assertEquals("ROLE_USUARIO",
                SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .iterator().next().getAuthority());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testNoAuthorizationHeader_ShouldNotSetAuthentication() throws Exception {
        given(jwtTokenUtil.getUsernameFromToken(null)).willReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    public void testNonBearerHeader_ShouldPassRawHeaderToTokenUtil() throws Exception {
        request.addHeader("Authorization", "Basic abc123");

        given(jwtTokenUtil.getUsernameFromToken("Basic abc123")).willReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtTokenUtil).getUsernameFromToken("Basic abc123");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testExpiredToken_ShouldNotSetAuthentication() throws Exception {
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);

        UserDetails userDetails = new User(USERNAME, "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USUARIO")));

        given(jwtTokenUtil.getUsernameFromToken(VALID_TOKEN)).willReturn(USERNAME);
        given(jwtTokenUtil.tokenValido(VALID_TOKEN)).willReturn(false);
        given(userDetailsService.loadUserByUsername(USERNAME)).willReturn(userDetails);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testAuthenticationAlreadyExists_ShouldNotOverwrite() throws Exception {
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);

        UsernamePasswordAuthenticationToken existingAuth =
                new UsernamePasswordAuthenticationToken("existing@acme.com", null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        given(jwtTokenUtil.getUsernameFromToken(VALID_TOKEN)).willReturn(USERNAME);

        filter.doFilterInternal(request, response, filterChain);

        assertEquals("existing@acme.com",
                SecurityContextHolder.getContext().getAuthentication().getName());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtTokenUtil, never()).tokenValido(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testMalformedToken_UsernameNull_ShouldNotSetAuthentication() throws Exception {
        request.addHeader("Authorization", "Bearer malformed.token");

        given(jwtTokenUtil.getUsernameFromToken("malformed.token")).willReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testFilterAlwaysContinuesChain() throws Exception {
        given(jwtTokenUtil.getUsernameFromToken(null)).willReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
