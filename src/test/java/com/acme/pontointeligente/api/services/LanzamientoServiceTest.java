package com.acme.pontointeligente.api.services;

import com.acme.pontointeligente.api.entities.Lanzamiento;
import com.acme.pontointeligente.api.repositories.LanzamientoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * Created by Ivan on 2/10/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LanzamientoServiceTest {

    @MockBean
    private LanzamientoRepository lanzamientoRepository;

    @Autowired
    private LanzamientoService lanzamientoService;

    @Before
    public void setUp() {
        given(lanzamientoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .willReturn(new PageImpl<>(new ArrayList<>()));
        given(lanzamientoRepository.findById(anyLong())).willReturn(Optional.of(new Lanzamiento()));
        given(lanzamientoRepository.save(any(Lanzamiento.class))).willReturn(new Lanzamiento());
    }

    @Test
    public void testBuscarPorFuncionarioId() throws Exception {
        Page<Lanzamiento> lanzamientos = this.lanzamientoService.buscarPorFuncionarioId(1L, PageRequest.of(0, 10));

        assertNotNull(lanzamientos);
        assertEquals(1, lanzamientos.getTotalPages());
        assertEquals(0, lanzamientos.getTotalElements());
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Optional<Lanzamiento> lanzamiento = this.lanzamientoService.buscarPorId(1L);
        assertTrue(lanzamiento.isPresent());
    }

    @Test
    public void testPersistir() throws Exception {
        Lanzamiento lanzamiento = this.lanzamientoService.persistir(new Lanzamiento());
        assertNotNull(lanzamiento);
    }

    @Test
    public void remover() throws Exception {
        this.lanzamientoService.remover(1L);
    }

}