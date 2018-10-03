package com.acme.pontointeligente.api.services;

import com.acme.pontointeligente.api.entities.Empresa;

import java.util.Optional;

/**
 * Created by Ivan on 2/10/2018.
 */
public interface EmpresaService {

    Optional<Empresa> buscarPorCnpj(String cnpj);

    Empresa persistir(Empresa empresa);

}
