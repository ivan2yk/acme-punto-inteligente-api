package com.acme.pontointeligente.api.controllers;

import com.acme.pontointeligente.api.dtos.LanzamientoDto;
import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.entities.Lanzamiento;
import com.acme.pontointeligente.api.enums.TipoEnum;
import com.acme.pontointeligente.api.response.Response;
import com.acme.pontointeligente.api.services.FuncionarioService;
import com.acme.pontointeligente.api.services.LanzamientoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Created by Ivan on 3/10/2018.
 */
@RestController
@RequestMapping("/api/lanzamientos")
@CrossOrigin(origins = "*")
public class LanzamientoController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private LanzamientoService lanzamientoService;
    private FuncionarioService funcionarioService;
    private Integer qtdPorPagina;

    @Autowired
    public LanzamientoController(LanzamientoService lanzamientoService,
                                 FuncionarioService funcionarioService,
                                 @Value("${pagination.qtd_por_pagina}") Integer qtdPorPagina) {
        this.lanzamientoService = lanzamientoService;
        this.funcionarioService = funcionarioService;
        this.qtdPorPagina = qtdPorPagina;
    }

    @GetMapping(value = "/funcionario/{id}")
    public ResponseEntity<Response<Page<LanzamientoDto>>> listarPorFuncionarioId(
            @PathVariable("id") Long id,
            @RequestParam(value = "pag", defaultValue = "0") Integer pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {
        logger.info("Buscando lanzamientos por id funcionario: {}, pagina: {}, qtdPorPagina: {}", id, pag, qtdPorPagina);
        Response<Page<LanzamientoDto>> pageResponse = new Response<>();

        PageRequest pageRequest = PageRequest.of(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord);

        Page<Lanzamiento> lanzamientos = this.lanzamientoService.buscarPorFuncionarioId(id, pageRequest);
        Page<LanzamientoDto> lanzamientoDtos = lanzamientos.map(this::convertToDto);
        pageResponse.setData(lanzamientoDtos);

        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LanzamientoDto>> listarPorId(@PathVariable("id") Long id) {
        logger.info("Buscando lanzamiento por id: {}", id);
        Response<LanzamientoDto> response = new Response<>();
        Optional<Lanzamiento> lanzamiento = this.lanzamientoService.buscarPorId(id);

        if (!lanzamiento.isPresent()) {
            logger.error("Lanzamiento con id: {} no fue encontrado", id);
            response.getErrors().add("Lanzamiento con id: " + id + " no fue encontrado");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convertToDto(lanzamiento.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response<LanzamientoDto>> adicionar(@Valid @RequestBody LanzamientoDto lanzamientoDto, BindingResult bindingResult) throws NoSuchAlgorithmException {
        logger.info("Adicionando lanzamiento: {}", lanzamientoDto);
        Response<LanzamientoDto> response = new Response<>();

        this.validarFuncionario(lanzamientoDto, bindingResult);
        Lanzamiento lanzamiento = this.convertDtoToLanzamiento(lanzamientoDto, bindingResult);

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> response.getErrors().add(objectError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Lanzamiento lanzamientoPersisted = this.lanzamientoService.persistir(lanzamiento);
        response.setData(this.convertToDto(lanzamientoPersisted));
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response<LanzamientoDto>> actualizar(@PathVariable("id") Long id, @Valid @RequestBody LanzamientoDto lanzamientoDto, BindingResult bindingResult) {
        logger.info("Actualizando lanzamiento: {}", lanzamientoDto);
        Response<LanzamientoDto> response = new Response<>();

        this.validarFuncionario(lanzamientoDto, bindingResult);
        lanzamientoDto.setId(Optional.of(id));

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> response.getErrors().add(objectError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        Lanzamiento lanzamiento = this.convertDtoToLanzamiento(lanzamientoDto, bindingResult);
        lanzamiento = this.lanzamientoService.persistir(lanzamiento);
        response.setData(this.convertToDto(lanzamiento));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
        logger.info("Removiendo lanzamiento: {}", id);
        Response<String> response = new Response<>();
        Optional<Lanzamiento> lanzamiento = this.lanzamientoService.buscarPorId(id);

        if (!lanzamiento.isPresent()) {
            logger.info("Error al remover lanzamiento con id: {}. No existe", id);
            response.getErrors().add("Error al remover, lanzamiento no existe");
            return ResponseEntity.badRequest().body(response);
        }
        this.lanzamientoService.remover(id);
        return ResponseEntity.ok(response);
    }

    private Lanzamiento convertDtoToLanzamiento(LanzamientoDto lanzamientoDto, BindingResult bindingResult) {
        Lanzamiento lanzamiento = new Lanzamiento();

        if (lanzamientoDto.getId().isPresent()) {
            Optional<Lanzamiento> l = this.lanzamientoService.buscarPorId(lanzamientoDto.getId().get());
            if (l.isPresent()) {
                lanzamiento = l.get();
            } else {
                bindingResult.addError(new ObjectError("lanzamiento", "Lanzamiento no encontrado"));
            }
        }

        if (lanzamientoDto.getFuncionarioId() != null) {
            lanzamiento.setFuncionario(new Funcionario());
            lanzamiento.getFuncionario().setId(lanzamientoDto.getFuncionarioId());
        }

        lanzamiento.setDescripcion(lanzamientoDto.getDescription());
        lanzamiento.setLocalizacion(lanzamientoDto.getLocalizacion());
        lanzamiento.setFecha(LocalDate.parse(lanzamientoDto.getData(), dateTimeFormatter));
        lanzamiento.setTipo(TipoEnum.valueOf(lanzamientoDto.getTipo()));

        return lanzamiento;
    }

    private void validarFuncionario(LanzamientoDto lanzamientoDto, BindingResult bindingResult) {
        if (lanzamientoDto.getFuncionarioId() == null) {
            bindingResult.addError(new ObjectError("funcionario", "Funcionario no existe"));
            return;
        }

        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lanzamientoDto.getFuncionarioId());
        if (!funcionario.isPresent()) {
            bindingResult.addError(new ObjectError("funcionario", "Funcionario no existe"));
        } else {
            lanzamientoDto.setFuncionarioId(funcionario.get().getId());
        }
    }

    private LanzamientoDto convertToDto(Lanzamiento lanzamiento) {
        LanzamientoDto lanzamientoDto = new LanzamientoDto();
        lanzamientoDto.setId(lanzamiento.getId() == null ? Optional.empty() : Optional.of(lanzamiento.getId()));
        lanzamientoDto.setData(lanzamiento.getFecha() == null ? null : lanzamiento.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE));
        lanzamientoDto.setTipo(Optional.ofNullable(lanzamiento.getTipo()).map(Enum::toString).orElse(null));
        lanzamientoDto.setDescription(lanzamiento.getDescripcion());
        lanzamientoDto.setLocalizacion(lanzamiento.getLocalizacion());
        lanzamientoDto.setFuncionarioId(lanzamiento.getFuncionario() == null ? null : lanzamiento.getFuncionario().getId());
        return lanzamientoDto;
    }

}