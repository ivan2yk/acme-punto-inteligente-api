package com.acme.pontointeligente.api.controllers;

import com.acme.pontointeligente.api.dtos.CadastroPFDto;
import com.acme.pontointeligente.api.entities.Empresa;
import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.enums.PerfilEnum;
import com.acme.pontointeligente.api.response.Response;
import com.acme.pontointeligente.api.services.EmpresaService;
import com.acme.pontointeligente.api.services.FuncionarioService;
import com.acme.pontointeligente.api.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private EmpresaService empresaService;
    private FuncionarioService funcionarioService;

    @Autowired
    public CadastroPFController(EmpresaService empresaService, FuncionarioService funcionarioService) {
        this.empresaService = empresaService;
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto, BindingResult bindingResult) throws NoSuchAlgorithmException {
        logger.info("Cadastrando PF: {}", cadastroPFDto);
        Response<CadastroPFDto> response = new Response<>();

        this.validarDatosExistentes(cadastroPFDto, bindingResult);
        Funcionario funcionario = this.convertDtoToFuncionario(cadastroPFDto);

        if (bindingResult.hasErrors()) {
            logger.error("Error al validar datos de cadastro PF: {}", bindingResult.getAllErrors());
            bindingResult.getAllErrors().forEach(objectError -> response.getErrors().add(objectError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        empresa.ifPresent(funcionario::setEmpresa);

        this.funcionarioService.persistir(funcionario);

        response.setData(this.convertToDto(funcionario));
        return ResponseEntity.ok(response);
    }

    private CadastroPFDto convertToDto(Funcionario funcionario) {
        CadastroPFDto cadastroPFDto = new CadastroPFDto();
        cadastroPFDto.setId(funcionario.getId());
        cadastroPFDto.setName(funcionario.getName());
        cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
        cadastroPFDto.setCpf(funcionario.getCpf());
        cadastroPFDto.setEmail(funcionario.getEmail());
        cadastroPFDto.setQtdHorasAlmuerzo(funcionario.getQtdHorasAlmuerzo() == null ? Optional.empty() : Optional.of(funcionario.getQtdHorasAlmuerzo() + ""));
        cadastroPFDto.setQtdHorasTrabajoDia(funcionario.getQtdHorasTrabajoDia() == null ? Optional.empty() : Optional.of(funcionario.getQtdHorasTrabajoDia() + ""));
        cadastroPFDto.setValorHora(funcionario.getValorHora() == null ? Optional.empty() : Optional.of(funcionario.getValorHora().toString()));
        return cadastroPFDto;
    }

    private Funcionario convertDtoToFuncionario(CadastroPFDto cadastroPFDto) {
        Funcionario funcionario = Funcionario.builder()
                .name(cadastroPFDto.getName())
                .email(cadastroPFDto.getEmail())
                .cpf(cadastroPFDto.getCpf())
                .perfil(PerfilEnum.ROLE_USUARIO)
                .senha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha())).build();

        cadastroPFDto.getQtdHorasAlmuerzo()
                .ifPresent(s -> funcionario.setQtdHorasAlmuerzo(Float.valueOf(s)));

        cadastroPFDto.getQtdHorasTrabajoDia()
                .ifPresent(s -> funcionario.setQtdHorasTrabajoDia(Float.valueOf(s)));

        cadastroPFDto.getValorHora()
                .ifPresent(s -> funcionario.setValorHora(new BigDecimal(s)));

        return funcionario;
    }

    private void validarDatosExistentes(CadastroPFDto cadastroPFDto, BindingResult bindingResult) {
        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        if (!empresa.isPresent()) {
            bindingResult.addError(new ObjectError("empresa", "Empresa no registrada"));
        }

        this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
                .ifPresent(funcionario -> bindingResult.addError(new ObjectError("funcionario", "CPF ya existe")));

        this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
                .ifPresent(funcionario -> bindingResult.addError(new ObjectError("funcionario", "Email ya existe")));
    }

}
