package com.acme.pontointeligente.api.services.impl;

import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.repositories.FuncionarioRepository;
import com.acme.pontointeligente.api.services.FuncionarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Ivan on 2/10/2018.
 */
@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private FuncionarioRepository funcionarioRepository;

    @Autowired
    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public Funcionario persistir(Funcionario funcionario) {
        logger.info("Persistiendo funcionario: {}", funcionario);
        return funcionarioRepository.save(funcionario);
    }

    @Override
    public Optional<Funcionario> buscarPorCpf(String cpf) {
        return Optional.ofNullable(funcionarioRepository.findByCpf(cpf));
    }

    @Override
    public Optional<Funcionario> buscarPorEmail(String email) {
        return Optional.ofNullable(funcionarioRepository.findByEmail(email));
    }

    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }
}
