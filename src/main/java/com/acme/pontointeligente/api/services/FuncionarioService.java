package com.acme.pontointeligente.api.services;

import com.acme.pontointeligente.api.entities.Funcionario;

import java.util.Optional;

/**
 * Created by Ivan on 2/10/2018.
 */
public interface FuncionarioService {

    Funcionario persistir(Funcionario funcionario);

    Optional<Funcionario> buscarPorCpf(String cpf);

    Optional<Funcionario> buscarPorEmail(String email);

    Optional<Funcionario> buscarPorId(Long id);

}
