package com.acme.pontointeligente.api.controllers;

import com.acme.pontointeligente.api.dtos.EmpresaDto;
import com.acme.pontointeligente.api.entities.Empresa;
import com.acme.pontointeligente.api.response.Response;
import com.acme.pontointeligente.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Ivan on 3/10/2018.
 */
@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private EmpresaService empresaService;

    @Autowired
    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping(value = "/cnpj/{cnpj}")
    public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable("cnpj") String cnpj) {
        logger.info("Buscando empresa por cnpj: {}", cnpj);
        Response<EmpresaDto> response = new Response<>();
        Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);

        if (!empresa.isPresent()) {
            logger.info("Empresa con cnpj: {} no fue encontrada", cnpj);
            return ResponseEntity.notFound().build();
        }

        response.setData(this.convertToEmpresaDto(empresa.get()));
        return ResponseEntity.ok(response);
    }

    private EmpresaDto convertToEmpresaDto(Empresa empresa) {
        EmpresaDto empresaDto = new EmpresaDto();
        empresaDto.setId(empresa.getId());
        empresaDto.setCnpj(empresa.getCnpj());
        empresaDto.setRazonSocial(empresa.getRazonSocial());
        return empresaDto;
    }

}
