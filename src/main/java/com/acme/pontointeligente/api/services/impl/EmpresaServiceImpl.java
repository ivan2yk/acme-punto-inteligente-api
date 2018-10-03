package com.acme.pontointeligente.api.services.impl;

import com.acme.pontointeligente.api.entities.Empresa;
import com.acme.pontointeligente.api.repositories.EmpresaRepository;
import com.acme.pontointeligente.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Ivan on 2/10/2018.
 */
@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private EmpresaRepository empresaRepository;

    @Autowired
    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        logger.info("Buscando empresa por CNPJ: {}", cnpj);
        return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
    }

    @Override
    public Empresa persistir(Empresa empresa) {
        return empresaRepository.save(empresa);
    }
}
