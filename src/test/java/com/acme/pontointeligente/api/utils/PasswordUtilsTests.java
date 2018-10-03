package com.acme.pontointeligente.api.utils;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ivan on 2/10/2018.
 */
public class PasswordUtilsTests {

    private static final String SENHA = "123456";
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testSenhaNula() {
        assertNull(PasswordUtils.gerarBCrypt(null));
    }

    @Test
    public void testGenerarHashSenha() {
        String hash = PasswordUtils.gerarBCrypt(SENHA);
        assertTrue(bCryptPasswordEncoder.matches(SENHA, hash));
    }

}
