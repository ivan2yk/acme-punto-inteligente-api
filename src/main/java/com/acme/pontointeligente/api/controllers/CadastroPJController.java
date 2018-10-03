package com.acme.pontointeligente.api.controllers;

import com.acme.pontointeligente.api.dtos.CadastroPJDto;
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
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ivan on 2/10/2018.
 */
@RestController
@RequestMapping("api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private FuncionarioService funcionarioService;
    private EmpresaService empresaService;

    @Autowired
    public CadastroPJController(FuncionarioService funcionarioService,
                                EmpresaService empresaService) {
        this.funcionarioService = funcionarioService;
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto, BindingResult bindingResult) throws NoSuchAlgorithmException {
        logger.info("Cadastrando PJ: {}", cadastroPJDto);
        Response<CadastroPJDto> response = new Response<>();

        this.validarDatosExistentes(cadastroPJDto, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.error("Error al validar datos cadastro JP: {}", bindingResult.getAllErrors());
            bindingResult.getAllErrors().forEach(objectError -> response.getErrors().add(objectError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Empresa empresa = this.convertDtoToEmpresa(cadastroPJDto);
        Funcionario funcionario = this.convertDtoToFuncionario(cadastroPJDto);

        this.empresaService.persistir(empresa);
        funcionario.setEmpresa(empresa);
        this.funcionarioService.persistir(funcionario);

        response.setData(this.convertToCadastroPJDto(funcionario));
        return ResponseEntity.ok(response);
    }

    private CadastroPJDto convertToCadastroPJDto(Funcionario funcionario) {
        CadastroPJDto dto = new CadastroPJDto();
        dto.setId(funcionario.getId());
        dto.setName(funcionario.getName());
        dto.setEmail(funcionario.getEmail());
        dto.setCpf(funcionario.getCpf());
        dto.setRazonSocial(funcionario.getEmpresa().getRazonSocial());
        dto.setCnpj(funcionario.getEmpresa().getCnpj());
        return dto;
    }

    private Funcionario convertDtoToFuncionario(CadastroPJDto cadastroPJDto) {
        return Funcionario.builder()
                .name(cadastroPJDto.getName())
                .email(cadastroPJDto.getEmail())
                .cpf(cadastroPJDto.getCpf())
                .perfil(PerfilEnum.ROLE_ADMIN)
                .senha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha())).build();
    }

    /**
     * Convierte DTO a entidad Empresa
     *
     * @param cadastroPJDto
     * @return
     */
    private Empresa convertDtoToEmpresa(CadastroPJDto cadastroPJDto) {
        return Empresa.builder()
                .cnpj(cadastroPJDto.getCnpj())
                .razonSocial(cadastroPJDto.getRazonSocial()).build();
    }

    /**
     * Verifica si la empresa y funciona se encuentran previamente registrados
     *
     * @param cadastroPJDto
     * @param bindingResult
     */
    private void validarDatosExistentes(CadastroPJDto cadastroPJDto, BindingResult bindingResult) {
        this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
                .ifPresent(empresa -> bindingResult.addError(new ObjectError("empresa", "Empresa ya existe.")));

        this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
                .ifPresent(funcionario -> bindingResult.addError(new ObjectError("funcionario", "CPF ya existe.")));

        this.funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
                .ifPresent(funcionario -> bindingResult.addError(new ObjectError("funcionario", "Email ya existe")));
    }

}
