package com.acme.pontointeligente.api.repositories;

import com.acme.pontointeligente.api.entities.Empresa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Ivan on 1/10/2018.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTests {

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String CNPJ = "51463645000100";

    @BeforeEach
    public void setUp() {
        Empresa empresa = new Empresa();
        empresa.setRazonSocial("Empresa Ejemplo");
        empresa.setCnpj(CNPJ);
        this.empresaRepository.save(empresa);
    }

    @AfterEach
    public final void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testFindByCnpjShouldPass() {
        Empresa empresa = this.empresaRepository.findByCnpj(CNPJ);
        assertEquals(CNPJ, empresa.getCnpj());
    }

}
