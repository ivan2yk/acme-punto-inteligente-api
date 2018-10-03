package com.acme.pontointeligente.api.services;

import com.acme.pontointeligente.api.entities.Lanzamiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Created by Ivan on 2/10/2018.
 */
public interface LanzamientoService {

    Page<Lanzamiento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);

    Optional<Lanzamiento> buscarPorId(Long id);

    Lanzamiento persistir(Lanzamiento lanzamiento);

    void remover(Long id);

}
