package com.acme.pontointeligente.api.controllers;

import com.acme.pontointeligente.api.dtos.LanzamientoDto;
import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.entities.Lanzamiento;
import com.acme.pontointeligente.api.enums.TipoEnum;
import com.acme.pontointeligente.api.services.FuncionarioService;
import com.acme.pontointeligente.api.services.LanzamientoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Ivan on 3/10/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LanzamientoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LanzamientoService lanzamientoService;

    @MockBean
    private FuncionarioService funcionarioService;

    private static final String URL_BASE = "/api/lanzamientos";
    private static final Long ID_FUNCIONARIO = 1L;
    private static final Long ID_LANZAMIENTO = 1L;
    private static final String TIPO = TipoEnum.INICIO_TRABAJO.toString();
    private static final LocalDate DATE = LocalDate.of(2018, 10, 3);

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    @WithMockUser
    public void testListarPorFuncionarioId() throws Exception {
        List<Lanzamiento> lanzamientos = Arrays.asList(
                Lanzamiento.builder()
                        .id(1L)
                        .localizacion("localizacion1")
                        .descripcion("descripcion1").build(),
                Lanzamiento.builder()
                        .id(2L)
                        .localizacion("localizacion2")
                        .descripcion("descripcion2").build());

        given(lanzamientoService.buscarPorFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class))).willReturn(new PageImpl<>(lanzamientos));

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/funcionario/" + "1" + "?pag=1&ord=id&dir=DESC").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].description").value("descripcion1"))
                .andExpect(jsonPath("$.data.content[1].description").value("descripcion2"));
    }

    @Test
    @WithMockUser
    public void testAdicionar() throws Exception {
        given(lanzamientoService.buscarPorId(anyLong())).willReturn(Optional.of(new Lanzamiento()));
        given(funcionarioService.buscarPorId(anyLong())).willReturn(Optional.of(new Funcionario()));
        given(lanzamientoService.persistir(any(Lanzamiento.class))).willReturn(this.getLanzamiento());

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(this.getJsonToPost())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID_LANZAMIENTO))
                .andExpect(jsonPath("$.data.data").value("2018-10-03"));
    }

    @Test
    public void testFormatDate() {
        assertNotNull(DATE.format(dateTimeFormatter));
    }

    @Test
    @WithMockUser
    public void testActualizar() throws Exception {
        given(funcionarioService.buscarPorId(anyLong())).willReturn(Optional.of(new Funcionario()));
        given(lanzamientoService.persistir(any(Lanzamiento.class))).willReturn(this.getLanzamiento());
        given(lanzamientoService.buscarPorId(ID_LANZAMIENTO)).willReturn(Optional.of(new Lanzamiento()));

        mvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/" + ID_LANZAMIENTO)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(this.getJsonToPut())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void remover() throws Exception {
        given(lanzamientoService.buscarPorId(anyLong())).willReturn(Optional.of(new Lanzamiento()));

        this.mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/" + ID_LANZAMIENTO)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vendor@acme.com", roles = {"VENDOR"})
    public void remover_Should_Return_Forbidden() throws Exception {
        given(lanzamientoService.buscarPorId(anyLong())).willReturn(Optional.of(new Lanzamiento()));

        this.mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/" + ID_LANZAMIENTO)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isForbidden());
    }

    private String getJsonToPost() throws JsonProcessingException {
        LanzamientoDto lanzamientoDto = new LanzamientoDto();
        lanzamientoDto.setId(null);
        lanzamientoDto.setFuncionarioId(ID_FUNCIONARIO);
        lanzamientoDto.setTipo(TIPO);
        lanzamientoDto.setData(DATE.format(dateTimeFormatter));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper.writeValueAsString(lanzamientoDto);
    }

    private Lanzamiento getLanzamiento() {
        return Lanzamiento.builder()
                .id(ID_LANZAMIENTO)
                .funcionario(Funcionario.builder().id(ID_FUNCIONARIO).build())
                .tipo(TipoEnum.valueOf(TIPO))
                .fecha(DATE)
                .build();
    }

    public String getJsonToPut() throws JsonProcessingException {
        LanzamientoDto lanzamientoDto = new LanzamientoDto();
        lanzamientoDto.setId(Optional.of(ID_LANZAMIENTO));
        lanzamientoDto.setFuncionarioId(ID_FUNCIONARIO);
        lanzamientoDto.setTipo(TIPO);
        lanzamientoDto.setData(DATE.format(dateTimeFormatter));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper.writeValueAsString(lanzamientoDto);
    }
}