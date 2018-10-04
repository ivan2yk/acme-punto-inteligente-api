package com.acme.pontointeligente.api.controllers;

import com.acme.pontointeligente.api.dtos.FuncionarioDto;
import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.response.Response;
import com.acme.pontointeligente.api.services.FuncionarioService;
import com.acme.pontointeligente.api.utils.PasswordUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Created by Ivan on 3/10/2018.
 */
@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class FuncionarioController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private FuncionarioService funcionarioService;

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<FuncionarioDto>> actualizar(@PathVariable("id") Long id,
                                                               @Valid @RequestBody FuncionarioDto funcionarioDto,
                                                               BindingResult bindingResult) throws NoSuchAlgorithmException {
        logger.info("Actualizando funcionario: {}", funcionarioDto);
        Response<FuncionarioDto> response = new Response<>();

        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
        if (!funcionario.isPresent()) {
            response.getErrors().add("Funcionario no existe");
            return ResponseEntity.badRequest().body(response);
        }

        this.actualizarDatosFuncionario(funcionario.get(), funcionarioDto, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.error("Error al validar los datos del funcionario. {}", bindingResult.getAllErrors());
            bindingResult.getAllErrors().forEach(objectError -> response.getErrors().add(objectError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.funcionarioService.persistir(funcionario.get());
        response.setData(this.convertToDto(funcionario.get()));
        return ResponseEntity.ok(response);
    }

    private FuncionarioDto convertToDto(Funcionario funcionario) {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setId(funcionario.getId());
        funcionarioDto.setEmail(funcionario.getEmail());
        funcionarioDto.setName(funcionario.getName());
        funcionarioDto.setQtdHorasTrabajoDia(funcionario.getQtdHorasTrabajoDia() == null ? Optional.empty() : Optional.of(Float.toString(funcionario.getQtdHorasTrabajoDia())));
        funcionarioDto.setQtdHorasAlmuerzo(funcionario.getQtdHorasAlmuerzo() == null ? Optional.empty() : Optional.of(Float.toString(funcionario.getQtdHorasAlmuerzo())));
        funcionarioDto.setValorHora(funcionario.getValorHora() == null ? Optional.empty() : Optional.of(funcionario.getValorHora().toString()));
        return funcionarioDto;
    }

    private void actualizarDatosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult bindingResult) throws NoSuchAlgorithmException {
        funcionario.setName(funcionarioDto.getName());

        if (!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
            this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
                    .ifPresent(f -> bindingResult.addError(new ObjectError("email", "Email ya existe")));
            funcionario.setEmail(funcionarioDto.getEmail());
        }

        funcionario.setQtdHorasAlmuerzo(null);
        funcionarioDto.getQtdHorasAlmuerzo()
                .ifPresent(s -> funcionario.setQtdHorasAlmuerzo(Float.valueOf(s)));

        funcionario.setQtdHorasTrabajoDia(null);
        funcionarioDto.getQtdHorasTrabajoDia()
                .ifPresent(s -> funcionario.setQtdHorasTrabajoDia(Float.valueOf(s)));

        funcionario.setValorHora(null);
        funcionarioDto.getValorHora().ifPresent(s -> funcionario.setValorHora(new BigDecimal(s)));

        if (funcionarioDto.getSenha().isPresent()) {
            funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
        }
    }

}
