package br.com.motiva.controller;

import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.service.TrechoRodoviaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrechoRodoviaController.class)
class TrechoRodoviaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrechoRodoviaService trechoService;

    private TrechoRodovia trecho;

    @BeforeEach
    void setUp() {
        trecho = new TrechoRodovia(250, 75.0, "UMIDO", 1);
    }

    @Test
    void listarTodos_DeveRetornar200ComListaDeTrechos() throws Exception {
        List<TrechoRodovia> trechos = Arrays.asList(
            new TrechoRodovia(250, 75.0, "UMIDO", 1),
            new TrechoRodovia(300, 50.0, "SECO", 2)
        );
        
        when(trechoService.listarTodos()).thenReturn(trechos);

        mockMvc.perform(get("/api/trechos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].km", is(250)))
            .andExpect(jsonPath("$[1].km", is(300)));

        verify(trechoService, times(1)).listarTodos();
    }

    @Test
    void buscarPorKm_QuandoTrechoExiste_DeveRetornar200ComTrecho() throws Exception {
        when(trechoService.buscarPorKm(250)).thenReturn(Optional.of(trecho));

        mockMvc.perform(get("/api/trechos/250"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.km", is(250)))
            .andExpect(jsonPath("$.alturaVegetacao", is(75.0)))
            .andExpect(jsonPath("$.tipoTerreno", is("UMIDO")));

        verify(trechoService, times(1)).buscarPorKm(250);
    }

    @Test
    void buscarPorKm_QuandoTrechoNaoExiste_DeveRetornar404() throws Exception {
        when(trechoService.buscarPorKm(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/trechos/999"))
            .andExpect(status().isNotFound());

        verify(trechoService, times(1)).buscarPorKm(999);
    }

    @Test
    void criar_QuandoDadosValidos_DeveRetornar201ComTrechoCriado() throws Exception {
        when(trechoService.criar(any(TrechoRodovia.class))).thenReturn(trecho);

        mockMvc.perform(post("/api/trechos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(trecho)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.km", is(250)))
            .andExpect(jsonPath("$.alturaVegetacao", is(75.0)));

        verify(trechoService, times(1)).criar(any(TrechoRodovia.class));
    }

    @Test
    void criar_QuandoKmNegativo_DeveRetornar400() throws Exception {
        TrechoRodovia trechoInvalido = new TrechoRodovia(-10, 75.0, "UMIDO", 1);
        when(trechoService.criar(any(TrechoRodovia.class)))
            .thenThrow(new IllegalArgumentException("km não pode ser negativo"));

        mockMvc.perform(post("/api/trechos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(trechoInvalido)))
            .andExpect(status().isBadRequest());

        verify(trechoService, times(1)).criar(any(TrechoRodovia.class));
    }

    @Test
    void atualizar_QuandoTrechoExiste_DeveRetornar200ComTrechoAtualizado() throws Exception {
        TrechoRodovia trechoAtualizado = new TrechoRodovia(250, 30.0, "SECO", 2);
        
        when(trechoService.atualizar(eq(250), any(TrechoRodovia.class))).thenReturn(trechoAtualizado);

        mockMvc.perform(put("/api/trechos/250")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(trechoAtualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.alturaVegetacao", is(30.0)))
            .andExpect(jsonPath("$.tipoTerreno", is("SECO")));

        verify(trechoService, times(1)).atualizar(eq(250), any(TrechoRodovia.class));
    }

    @Test
    void atualizar_QuandoTrechoNaoExiste_DeveRetornar404() throws Exception {
        TrechoRodovia trechoAtualizado = new TrechoRodovia(999, 30.0, "SECO", 2);
        
        when(trechoService.atualizar(eq(999), any(TrechoRodovia.class)))
            .thenThrow(new RuntimeException("Trecho não encontrado com KM: 999"));

        mockMvc.perform(put("/api/trechos/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(trechoAtualizado)))
            .andExpect(status().isNotFound());

        verify(trechoService, times(1)).atualizar(eq(999), any(TrechoRodovia.class));
    }

    @Test
    void deletar_QuandoTrechoExiste_DeveRetornar204() throws Exception {
        doNothing().when(trechoService).deletar(250);

        mockMvc.perform(delete("/api/trechos/250"))
            .andExpect(status().isNoContent());

        verify(trechoService, times(1)).deletar(250);
    }

    @Test
    void deletar_QuandoTrechoNaoExiste_DeveRetornar404() throws Exception {
        doThrow(new RuntimeException("Trecho não encontrado com KM: 999"))
            .when(trechoService).deletar(999);

        mockMvc.perform(delete("/api/trechos/999"))
            .andExpect(status().isNotFound());

        verify(trechoService, times(1)).deletar(999);
    }

    @Test
    void buscarPorTipoTerreno_DeveRetornar200ComTrechos() throws Exception {
        List<TrechoRodovia> trechos = Arrays.asList(
            new TrechoRodovia(250, 75.0, "UMIDO", 1),
            new TrechoRodovia(300, 80.0, "UMIDO", 2)
        );
        
        when(trechoService.buscarPorTipoTerreno("UMIDO")).thenReturn(trechos);

        mockMvc.perform(get("/api/trechos/tipo/UMIDO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].tipoTerreno", is("UMIDO")))
            .andExpect(jsonPath("$[1].tipoTerreno", is("UMIDO")));

        verify(trechoService, times(1)).buscarPorTipoTerreno("UMIDO");
    }

    @Test
    void buscarPorAlturaMinima_DeveRetornar200ComTrechos() throws Exception {
        List<TrechoRodovia> trechos = Arrays.asList(
            new TrechoRodovia(250, 75.0, "UMIDO", 1),
            new TrechoRodovia(300, 80.0, "UMIDO", 2)
        );
        
        when(trechoService.buscarPorAlturaMinima(50.0)).thenReturn(trechos);

        mockMvc.perform(get("/api/trechos/altura-minima/50.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].alturaVegetacao", greaterThan(50.0)))
            .andExpect(jsonPath("$[1].alturaVegetacao", greaterThan(50.0)));

        verify(trechoService, times(1)).buscarPorAlturaMinima(50.0);
    }
}
