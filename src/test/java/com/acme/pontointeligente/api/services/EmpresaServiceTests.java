package com.acme.pontointeligente.api.services;

import com.acme.pontointeligente.api.entities.Empresa;
import com.acme.pontointeligente.api.repositories.EmpresaRepository;
import com.acme.pontointeligente.api.services.impl.EmpresaServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Ivan on 2/10/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTests {

    @Mock
    private EmpresaRepository empresaRepository;

    private EmpresaService empresaService;

    private static final String CNPJ = "9876543210";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        empresaService = new EmpresaServiceImpl(empresaRepository);

        when(empresaRepository.findByCnpj(anyString())).thenReturn(new Empresa());
        when(empresaRepository.save(Mockito.any(Empresa.class))).thenReturn(new Empresa());
    }

    @Test
    public void testBuscarPorCnpj() {
        Optional<Empresa> empresa = empresaService.buscarPorCnpj(CNPJ);
        assertTrue(empresa.isPresent());
    }

    @Test
    public void testPersistir() {
        Empresa e = new Empresa();
        Empresa empresa = empresaService.persistir(e);

        assertNotNull(empresa);
        verify(empresaRepository, times(1)).save(e);
    }

}
