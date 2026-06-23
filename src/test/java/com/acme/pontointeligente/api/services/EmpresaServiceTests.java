package com.acme.pontointeligente.api.services;

import com.acme.pontointeligente.api.entities.Empresa;
import com.acme.pontointeligente.api.repositories.EmpresaRepository;
import com.acme.pontointeligente.api.services.impl.EmpresaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Ivan on 2/10/2018.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmpresaServiceTests {

    @Mock
    private EmpresaRepository empresaRepository;

    private EmpresaService empresaService;

    private static final String CNPJ = "9876543210";

    @BeforeEach
    public void setUp() {
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
