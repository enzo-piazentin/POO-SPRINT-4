package br.com.motiva.controller;

import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.service.EquipeManutencaoService;
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

@WebMvcTest(EquipeManutencaoController.class)
class EquipeManutencaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EquipeManutencaoService equipeService;

    private EquipeManutencao equipe;

    @BeforeEach
    void setUp() {
        equipe = new EquipeManutencao(1, "Equipe Leste", "ROCADA", "LESTE");
    }

    @Test
    void listarTodos_DeveRetornar200ComListaDeEquipes() throws Exception {
        List<EquipeManutencao> equipes = Arrays.asList(
            new EquipeManutencao(1, "Equipe Leste", "ROCADA", "LESTE"),
            new EquipeManutencao(2, "Equipe Norte", "PODA", "NORTE")
        );
        
        when(equipeService.listarTodos()).thenReturn(equipes);

        mockMvc.perform(get("/api/equipes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].nome", is("Equipe Leste")))
            .andExpect(jsonPath("$[1].nome", is("Equipe Norte")));

        verify(equipeService, times(1)).listarTodos();
    }

    @Test
    void buscarPorId_QuandoEquipeExiste_DeveRetornar200ComEquipe() throws Exception {
        when(equipeService.buscarPorId(1)).thenReturn(Optional.of(equipe));

        mockMvc.perform(get("/api/equipes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.nome", is("Equipe Leste")))
            .andExpect(jsonPath("$.especialidade", is("ROCADA")))
            .andExpect(jsonPath("$.direcao", is("LESTE")));

        verify(equipeService, times(1)).buscarPorId(1);
    }

    @Test
    void buscarPorId_QuandoEquipeNaoExiste_DeveRetornar404() throws Exception {
        when(equipeService.buscarPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/equipes/999"))
            .andExpect(status().isNotFound());

        verify(equipeService, times(1)).buscarPorId(999);
    }

    @Test
    void criar_QuandoDadosValidos_DeveRetornar201ComEquipeCriada() throws Exception {
        when(equipeService.criar(any(EquipeManutencao.class))).thenReturn(equipe);

        mockMvc.perform(post("/api/equipes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(equipe)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.nome", is("Equipe Leste")));

        verify(equipeService, times(1)).criar(any(EquipeManutencao.class));
    }

    @Test
    void criar_QuandoNomeVazio_DeveRetornar400() throws Exception {
        EquipeManutencao equipeInvalida = new EquipeManutencao();
        equipeInvalida.setId(1);
        equipeInvalida.setNome("");
        equipeInvalida.setEspecialidade("ROCADA");
        equipeInvalida.setDirecao("LESTE");

        mockMvc.perform(post("/api/equipes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(equipeInvalida)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void atualizar_QuandoEquipeExiste_DeveRetornar200ComEquipeAtualizada() throws Exception {
        EquipeManutencao equipeAtualizada = new EquipeManutencao(1, "Equipe Oeste", "PODA", "OESTE");
        
        when(equipeService.atualizar(eq(1), any(EquipeManutencao.class))).thenReturn(equipeAtualizada);

        mockMvc.perform(put("/api/equipes/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(equipeAtualizada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Equipe Oeste")))
            .andExpect(jsonPath("$.especialidade", is("PODA")));

        verify(equipeService, times(1)).atualizar(eq(1), any(EquipeManutencao.class));
    }

    @Test
    void atualizar_QuandoEquipeNaoExiste_DeveRetornar404() throws Exception {
        EquipeManutencao equipeAtualizada = new EquipeManutencao(999, "Equipe Oeste", "PODA", "OESTE");
        
        when(equipeService.atualizar(eq(999), any(EquipeManutencao.class)))
            .thenThrow(new RuntimeException("Equipe não encontrada com ID: 999"));

        mockMvc.perform(put("/api/equipes/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(equipeAtualizada)))
            .andExpect(status().isNotFound());

        verify(equipeService, times(1)).atualizar(eq(999), any(EquipeManutencao.class));
    }

    @Test
    void deletar_QuandoEquipeExiste_DeveRetornar204() throws Exception {
        doNothing().when(equipeService).deletar(1);

        mockMvc.perform(delete("/api/equipes/1"))
            .andExpect(status().isNoContent());

        verify(equipeService, times(1)).deletar(1);
    }

    @Test
    void deletar_QuandoEquipeNaoExiste_DeveRetornar404() throws Exception {
        doThrow(new RuntimeException("Equipe não encontrada com ID: 999"))
            .when(equipeService).deletar(999);

        mockMvc.perform(delete("/api/equipes/999"))
            .andExpect(status().isNotFound());

        verify(equipeService, times(1)).deletar(999);
    }

    @Test
    void buscarPorEspecialidade_DeveRetornar200ComEquipes() throws Exception {
        List<EquipeManutencao> equipes = Arrays.asList(
            new EquipeManutencao(1, "Equipe Leste", "ROCADA", "LESTE"),
            new EquipeManutencao(2, "Equipe Norte", "ROCADA", "NORTE")
        );
        
        when(equipeService.buscarPorEspecialidade("ROCADA")).thenReturn(equipes);

        mockMvc.perform(get("/api/equipes/especialidade/ROCADA"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].especialidade", is("ROCADA")))
            .andExpect(jsonPath("$[1].especialidade", is("ROCADA")));

        verify(equipeService, times(1)).buscarPorEspecialidade("ROCADA");
    }

    @Test
    void buscarPorDirecao_DeveRetornar200ComEquipes() throws Exception {
        List<EquipeManutencao> equipes = Arrays.asList(
            new EquipeManutencao(1, "Equipe Leste", "ROCADA", "LESTE"),
            new EquipeManutencao(3, "Equipe Sudeste", "PODA", "LESTE")
        );
        
        when(equipeService.buscarPorDirecao("LESTE")).thenReturn(equipes);

        mockMvc.perform(get("/api/equipes/direcao/LESTE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].direcao", is("LESTE")))
            .andExpect(jsonPath("$[1].direcao", is("LESTE")));

        verify(equipeService, times(1)).buscarPorDirecao("LESTE");
    }
}
