package com.acme.pontointeligente.api.repositories;

import com.acme.pontointeligente.api.entities.Lanzamiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

/**
 * Created by Ivan on 1/10/2018.
 */
@Repository
@Transactional(readOnly = true)
@NamedQueries({
        @NamedQuery(name = "LanzamientoRepository.findByFuncionarioId",
                query = "select l from Lanzamiento l where l.funcionario.id = :funcionarioId")
})
public interface LanzamientoRepository extends JpaRepository<Lanzamiento, Long> {

    List<Lanzamiento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);

    Page<Lanzamiento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable pageable);

}
