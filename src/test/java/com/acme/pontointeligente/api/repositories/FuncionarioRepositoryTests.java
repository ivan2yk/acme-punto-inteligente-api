package com.acme.pontointeligente.api.repositories;

import com.acme.pontointeligente.api.entities.Empresa;
import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.enums.PerfilEnum;
import com.acme.pontointeligente.api.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Ivan on 1/10/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTests {

    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String EMAIL = "leivagarcia18@gmail.com";
    private static final String CPF = "46567850";

    @Before
    public void setUp() {
        Empresa empresa = Empresa.builder()
                .razonSocial("Empresa Ejemplo")
                .cnpj("51463645000100")
                .build();

        this.empresaRepository.save(empresa);

        this.funcionarioRepository.save(Funcionario.builder()
                .name("Ivan")
                .perfil(PerfilEnum.ROLE_USUARIO)
                .senha(PasswordUtils.gerarBCrypt("123456"))
                .cpf(CPF)
                .email(EMAIL)
                .empresa(empresa)
                .build());
    }

    @After
    public void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testfindByEmail() {
        Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);
        assertEquals(EMAIL, funcionario.getEmail());
    }

    @Test
    public void testFindByCpf() {
        Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);
        assertEquals(CPF, funcionario.getCpf());
    }

    @Test
    public void testFindByCpfOrEmail() {
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
        assertNotNull(funcionario);
    }

}
