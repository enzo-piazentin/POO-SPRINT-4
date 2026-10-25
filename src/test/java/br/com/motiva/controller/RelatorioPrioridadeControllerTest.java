package br.com.motiva.controller;

import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.service.RelatorioPrioridadeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RelatorioPrioridadeController.class)
class RelatorioPrioridadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RelatorioPrioridadeService relatorioService;

    private RelatorioPrioridade relatorio;

    @BeforeEach
    void setUp() {
        relatorio = new RelatorioPrioridade(1, 2, 3, 4, "Resumo do relatório");
    }

    @Test
    void gerarRelatorio_DeveRetornar201ComRelatorioGerado() throws Exception {
        when(relatorioService.gerarRelatorio()).thenReturn(relatorio);

        mockMvc.perform(post("/api/relatorios"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.urgentes", is(1)))
            .andExpect(jsonPath("$.criticos", is(2)))
            .andExpect(jsonPath("$.atencao", is(3)))
            .andExpect(jsonPath("$.normais", is(4)))
            .andExpect(jsonPath("$.resumo", notNullValue()))
            .andExpect(jsonPath("$.geradoEm", notNullValue()));

        verify(relatorioService, times(1)).gerarRelatorio();
    }

    @Test
    void listarHistorico_DeveRetornar200ComListaDeRelatorios() throws Exception {
        List<RelatorioPrioridade> relatorios = Arrays.asList(
            new RelatorioPrioridade(1, 2, 3, 4, "Resumo 1"),
            new RelatorioPrioridade(2, 1, 1, 5, "Resumo 2")
        );
        
        when(relatorioService.listarTodos()).thenReturn(relatorios);

        mockMvc.perform(get("/api/relatorios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].urgentes", is(1)))
            .andExpect(jsonPath("$[1].urgentes", is(2)));

        verify(relatorioService, times(1)).listarTodos();
    }

    @Test
    void buscarPorPeriodo_DeveRetornar200ComRelatoriosNoPeriodo() throws Exception {
        List<RelatorioPrioridade> relatorios = Arrays.asList(
            new RelatorioPrioridade(1, 2, 3, 4, "Resumo 1")
        );
        
        when(relatorioService.buscarPorPeriodo(any(), any())).thenReturn(relatorios);

        mockMvc.perform(get("/api/relatorios/periodo")
            .param("inicio", "2024-01-01T00:00:00")
            .param("fim", "2024-12-31T23:59:59"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].urgentes", is(1)));

        verify(relatorioService, times(1)).buscarPorPeriodo(any(), any());
    }

    @Test
    void buscarPorPeriodo_SemParametros_DeveRetornar400() throws Exception {
        mockMvc.perform(get("/api/relatorios/periodo"))
            .andExpect(status().isBadRequest());
    }
}
