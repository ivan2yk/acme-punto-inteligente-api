package com.acme.pontointeligente.api.services.impl;

import com.acme.pontointeligente.api.entities.Lanzamiento;
import com.acme.pontointeligente.api.repositories.LanzamientoRepository;
import com.acme.pontointeligente.api.services.LanzamientoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Ivan on 2/10/2018.
 */
@Service
public class LanzamientoServiceImpl implements LanzamientoService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private LanzamientoRepository lanzamientoRepository;

    @Autowired
    public LanzamientoServiceImpl(LanzamientoRepository lanzamientoRepository) {
        this.lanzamientoRepository = lanzamientoRepository;
    }

    @Override
    public Page<Lanzamiento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
        logger.info("Buscando lanzamientos para funcionario: {}", funcionarioId);
        return lanzamientoRepository.findByFuncionarioId(funcionarioId, pageRequest);
    }

    @Override
    @Cacheable("lanzamientoPorId")
    public Optional<Lanzamiento> buscarPorId(Long id) {
        logger.info("---buscarPorId: {}", id);
        return lanzamientoRepository.findById(id);
    }

    @Override
    @CachePut("lanzamientoPorId")
    public Lanzamiento persistir(Lanzamiento lanzamiento) {
        return lanzamientoRepository.save(lanzamiento);
    }

    @Override
    public void remover(Long id) {
        lanzamientoRepository.deleteById(id);
    }
}
