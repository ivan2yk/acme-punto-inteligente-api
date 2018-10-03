package com.acme.pontointeligente.api.controllers;

import com.acme.pontointeligente.api.entities.Empresa;
import com.acme.pontointeligente.api.services.EmpresaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

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
public class EmpresaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmpresaService empresaService;

    private static final String BUSCAR_EMPRESA_URL = "/api/empresas/cnpj/";
    private static final Long ID = Long.valueOf(1);
    private static final String CNPJ = "20377339461";
    private static final String RAZON_SOCIAL = "Empresa XYZ";

    @Test
    @WithMockUser
    public void testBuscarPorCnpj_Should_Return_NotFound() throws Exception {
        given(empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testBuscarPorCnpj_Should_Return_Found() throws Exception {
        given(empresaService.buscarPorCnpj(CNPJ)).willReturn(Optional.of(Empresa.builder()
                .cnpj(CNPJ)
                .razonSocial(RAZON_SOCIAL)
                .id(ID)
                .build()));

        mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.razonSocial").value(RAZON_SOCIAL))
                .andExpect(jsonPath("$.data.cnpj").value(CNPJ))
                .andExpect(jsonPath("$.errors").isEmpty());
    }


}