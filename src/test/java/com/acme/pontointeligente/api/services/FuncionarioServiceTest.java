package com.acme.pontointeligente.api.services;

import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.repositories.FuncionarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Ivan on 2/10/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {

    @MockBean
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;

    @Before
    public void setUp() throws Exception {
        Funcionario funcionario = new Funcionario();
        given(funcionarioRepository.save(any(Funcionario.class))).willReturn(funcionario);
        given(funcionarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(funcionario));
        given(funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(funcionario);
        given(funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(funcionario);
    }

    @Test
    public void testPersistir() throws Exception {
        Funcionario f = new Funcionario();
        Funcionario funcionario = this.funcionarioService.persistir(f);
        assertNotNull(funcionario);
        verify(funcionarioRepository, times(1)).save(f);
    }

    @Test
    public void testBuscarPorCpf() throws Exception {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf("123");
        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testBuscarPorEmail() throws Exception {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail("leivagarcia18@gmail.com");
        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(1l);
        assertTrue(funcionario.isPresent());
    }

}