package com.acme.pontointeligente.api.controllers;

import com.acme.pontointeligente.api.dtos.FuncionarioDto;
import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.services.FuncionarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Ivan on 25/06/2026.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FuncionarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FuncionarioService funcionarioService;

    private static final String UPDATE_FUNCIONARIO_URL = "/api/funcionarios/";
    private static final Long ID = 1L;
    private static final String NAME = "Juan Perez";
    private static final String EMAIL = "juan@example.com";
    private static final String NEW_EMAIL = "juannew@example.com";
    private static final String NEW_NAME = "Juan Perez Updated";
    private static final Float QTD_HORAS_TRABAJO = 8.0f;
    private static final Float QTD_HORAS_ALMUERZO = 1.0f;
    private static final BigDecimal VALOR_HORA = new BigDecimal("100.00");

    /**
     * Test: Update funcionario successfully with all fields
     */
    @Test
    @WithMockUser
    public void testActualizar_Success_AllFields() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .qtdHorasTrabajoDia(QTD_HORAS_TRABAJO)
                .qtdHorasAlmuerzo(QTD_HORAS_ALMUERZO)
                .valorHora(VALOR_HORA)
                .build();

        Funcionario funcionarioUpdated = Funcionario.builder()
                .id(ID)
                .name(NEW_NAME)
                .email(EMAIL)
                .qtdHorasTrabajoDia(QTD_HORAS_TRABAJO)
                .qtdHorasAlmuerzo(QTD_HORAS_ALMUERZO)
                .valorHora(VALOR_HORA)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(EMAIL);
        funcionarioDto.setQtdHorasTrabajoDia(Optional.of("8.0"));
        funcionarioDto.setQtdHorasAlmuerzo(Optional.of("1.0"));
        funcionarioDto.setValorHora(Optional.of("100.00"));
        funcionarioDto.setSenha(Optional.empty());

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.buscarPorEmail(EMAIL)).willReturn(Optional.of(funcionario));
        given(funcionarioService.persistir(any(Funcionario.class))).willReturn(funcionarioUpdated);

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.name").value(NEW_NAME))
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    /**
     * Test: Update funcionario - funcionario not found
     */
    @Test
    @WithMockUser
    public void testActualizar_FuncionarioNotFound() throws Exception {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(EMAIL);

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Funcionario no existe"));
    }

    /**
     * Test: Update funcionario - validation error (empty name)
     */
    @Test
    @WithMockUser
    public void testActualizar_ValidationError_EmptyName() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName("");
        funcionarioDto.setEmail(EMAIL);

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test: Update funcionario - validation error (empty email)
     */
    @Test
    @WithMockUser
    public void testActualizar_ValidationError_EmptyEmail() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail("");

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test: Update funcionario with new email - email already exists
     */
    @Test
    @WithMockUser
    public void testActualizar_EmailAlreadyExists() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .build();

        Funcionario otherFuncionario = Funcionario.builder()
                .id(2L)
                .name("Other")
                .email(NEW_EMAIL)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(NEW_EMAIL);

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.buscarPorEmail(NEW_EMAIL)).willReturn(Optional.of(otherFuncionario));

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Email ya existe"));
    }

    /**
     * Test: Update funcionario - email changed to a new email (not existing)
     */
    @Test
    @WithMockUser
    public void testActualizar_EmailChanged_NewEmailNotExisting() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .qtdHorasTrabajoDia(QTD_HORAS_TRABAJO)
                .qtdHorasAlmuerzo(QTD_HORAS_ALMUERZO)
                .valorHora(VALOR_HORA)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(NEW_EMAIL);

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.buscarPorEmail(NEW_EMAIL)).willReturn(Optional.empty());
        given(funcionarioService.persistir(any(Funcionario.class))).willReturn(funcionario);

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test: Update funcionario with password
     */
    @Test
    @WithMockUser
    public void testActualizar_WithPassword() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(EMAIL);
        funcionarioDto.setSenha(Optional.of("newpassword123"));

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.persistir(any(Funcionario.class))).willReturn(funcionario);

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test: Update funcionario - set optional fields to empty
     */
    @Test
    @WithMockUser
    public void testActualizar_SetOptionalFieldsEmpty() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .qtdHorasTrabajoDia(QTD_HORAS_TRABAJO)
                .qtdHorasAlmuerzo(QTD_HORAS_ALMUERZO)
                .valorHora(VALOR_HORA)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(EMAIL);
        funcionarioDto.setQtdHorasTrabajoDia(Optional.empty());
        funcionarioDto.setQtdHorasAlmuerzo(Optional.empty());
        funcionarioDto.setValorHora(Optional.empty());

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.persistir(any(Funcionario.class))).willReturn(funcionario);

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test: Update funcionario - with only required fields
     */
    @Test
    @WithMockUser
    public void testActualizar_OnlyRequiredFields() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(EMAIL);

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.persistir(any(Funcionario.class))).willReturn(funcionario);

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    /**
     * Test: convertToDto with all null optional fields
     */
    @Test
    @WithMockUser
    public void testActualizar_ConvertToDtoWithNullOptionals() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .qtdHorasTrabajoDia(null)
                .qtdHorasAlmuerzo(null)
                .valorHora(null)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NAME);
        funcionarioDto.setEmail(EMAIL);

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.persistir(any(Funcionario.class))).willReturn(funcionario);

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.qtdHorasTrabajoDia").isEmpty())
                .andExpect(jsonPath("$.data.qtdHorasAlmuerzo").isEmpty())
                .andExpect(jsonPath("$.data.valorHora").isEmpty());
    }

    /**
     * Test: Update funcionario with decimal values for optional fields
     */
    @Test
    @WithMockUser
    public void testActualizar_WithDecimalValues() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .qtdHorasTrabajoDia(7.5f)
                .qtdHorasAlmuerzo(0.5f)
                .valorHora(new BigDecimal("50.50"))
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(EMAIL);
        funcionarioDto.setQtdHorasTrabajoDia(Optional.of("7.5"));
        funcionarioDto.setQtdHorasAlmuerzo(Optional.of("0.5"));
        funcionarioDto.setValorHora(Optional.of("50.50"));

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.persistir(any(Funcionario.class))).willReturn(funcionario);

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test: Update funcionario - same email (no change)
     */
    @Test
    @WithMockUser
    public void testActualizar_SameEmail() throws Exception {
        Funcionario funcionario = Funcionario.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .build();

        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setName(NEW_NAME);
        funcionarioDto.setEmail(EMAIL);

        given(funcionarioService.buscarPorId(ID)).willReturn(Optional.of(funcionario));
        given(funcionarioService.persistir(any(Funcionario.class))).willReturn(funcionario);

        mvc.perform(MockMvcRequestBuilders.put(UPDATE_FUNCIONARIO_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonToPut(funcionarioDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Helper method to convert DTO to JSON
     */
    private String getJsonToPut(FuncionarioDto funcionarioDto) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper.writeValueAsString(funcionarioDto);
    }

}
