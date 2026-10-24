package br.com.motiva.service;

import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.RelatorioPrioridadeRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioPrioridadeServiceTest {

    @Mock
    private RelatorioPrioridadeRepository relatorioRepository;

    @Mock
    private TrechoRodoviaRepository trechoRepository;

    @InjectMocks
    private RelatorioPrioridadeService relatorioService;

    private List<TrechoRodovia> trechos;

    @BeforeEach
    void setUp() {
        trechos = Arrays.asList(
            new TrechoRodovia(100, 120.0, "UMIDO", 1),
            new TrechoRodovia(200, 70.0, "SECO", 2),
            new TrechoRodovia(300, 45.0, "UMIDO", 1),
            new TrechoRodovia(400, 20.0, "SECO", 2)
        );
    }

    @Test
    void listarTodos_DeveRetornarRelatoriosOrdenadosPorData() {
        List<RelatorioPrioridade> relatorios = Arrays.asList(
            new RelatorioPrioridade(1, 2, 3, 4, "Resumo 1"),
            new RelatorioPrioridade(2, 1, 1, 5, "Resumo 2")
        );
        
        when(relatorioRepository.findAllByOrderByGeradoEmDesc()).thenReturn(relatorios);

        List<RelatorioPrioridade> resultado = relatorioService.listarTodos();

        assertEquals(2, resultado.size());
        verify(relatorioRepository, times(1)).findAllByOrderByGeradoEmDesc();
    }

    @Test
    void gerarRelatorio_DeveClassificarTrechosCorretamente() {
        when(trechoRepository.findAll()).thenReturn(trechos);
        when(relatorioRepository.save(any(RelatorioPrioridade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RelatorioPrioridade resultado = relatorioService.gerarRelatorio();

        assertNotNull(resultado);
        assertEquals(1, resultado.getUrgentes()); // 120cm >= 100
        assertEquals(1, resultado.getCriticos()); // 70cm >= 60
        assertEquals(1, resultado.getAtencao()); // 45cm >= 30
        assertEquals(1, resultado.getNormais()); // 20cm < 30
        assertNotNull(resultado.getResumo());
        assertNotNull(resultado.getGeradoEm());
        
        verify(trechoRepository, times(1)).findAll();
        verify(relatorioRepository, times(1)).save(any(RelatorioPrioridade.class));
    }

    @Test
    void gerarRelatorio_ComListaVazia_DeveGerarRelatorioZerado() {
        when(trechoRepository.findAll()).thenReturn(Arrays.asList());
        when(relatorioRepository.save(any(RelatorioPrioridade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RelatorioPrioridade resultado = relatorioService.gerarRelatorio();

        assertNotNull(resultado);
        assertEquals(0, resultado.getUrgentes());
        assertEquals(0, resultado.getCriticos());
        assertEquals(0, resultado.getAtencao());
        assertEquals(0, resultado.getNormais());
        
        verify(trechoRepository, times(1)).findAll();
        verify(relatorioRepository, times(1)).save(any(RelatorioPrioridade.class));
    }

    @Test
    void gerarRelatorio_DeveIncluirResumoComDetalhes() {
        when(trechoRepository.findAll()).thenReturn(trechos);
        when(relatorioRepository.save(any(RelatorioPrioridade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RelatorioPrioridade resultado = relatorioService.gerarRelatorio();

        assertNotNull(resultado.getResumo());
        assertTrue(resultado.getResumo().contains("RELATÓRIO DE PRIORIDADE"));
        assertTrue(resultado.getResumo().contains("KM 100"));
        assertTrue(resultado.getResumo().contains("URGENTE"));
        assertTrue(resultado.getResumo().contains("RESUMO"));
    }

    @Test
    void buscarPorPeriodo_DeveRetornarRelatoriosNoPeriodo() {
        LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2024, 12, 31, 23, 59);
        
        List<RelatorioPrioridade> relatorios = Arrays.asList(
            new RelatorioPrioridade(1, 2, 3, 4, "Resumo 1")
        );
        
        when(relatorioRepository.findByGeradoEmBetween(inicio, fim)).thenReturn(relatorios);

        List<RelatorioPrioridade> resultado = relatorioService.buscarPorPeriodo(inicio, fim);

        assertEquals(1, resultado.size());
        verify(relatorioRepository, times(1)).findByGeradoEmBetween(inicio, fim);
    }

    @Test
    void classificarPrioridade_Altura120_DeveRetornarUrgente() {
        when(trechoRepository.findAll()).thenReturn(Arrays.asList(new TrechoRodovia(100, 120.0, "UMIDO", 1)));
        when(relatorioRepository.save(any(RelatorioPrioridade.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        RelatorioPrioridade resultado = relatorioService.gerarRelatorio();
        assertEquals(1, resultado.getUrgentes());
    }

    @Test
    void classificarPrioridade_Altura70_DeveRetornarCritico() {
        when(trechoRepository.findAll()).thenReturn(Arrays.asList(new TrechoRodovia(200, 70.0, "SECO", 2)));
        when(relatorioRepository.save(any(RelatorioPrioridade.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        RelatorioPrioridade resultado = relatorioService.gerarRelatorio();
        assertEquals(1, resultado.getCriticos());
    }

    @Test
    void classificarPrioridade_Altura45_DeveRetornarAtencao() {
        when(trechoRepository.findAll()).thenReturn(Arrays.asList(new TrechoRodovia(300, 45.0, "UMIDO", 1)));
        when(relatorioRepository.save(any(RelatorioPrioridade.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        RelatorioPrioridade resultado = relatorioService.gerarRelatorio();
        assertEquals(1, resultado.getAtencao());
    }

    @Test
    void classificarPrioridade_Altura20_DeveRetornarNormal() {
        when(trechoRepository.findAll()).thenReturn(Arrays.asList(new TrechoRodovia(400, 20.0, "SECO", 2)));
        when(relatorioRepository.save(any(RelatorioPrioridade.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        RelatorioPrioridade resultado = relatorioService.gerarRelatorio();
        assertEquals(1, resultado.getNormais());
    }

    @Test
    void gerarRelatorio_ComMultiplosTrechosMesmaPrioridade_DeveContarCorretamente() {
        List<TrechoRodovia> trechosUrgentes = Arrays.asList(
            new TrechoRodovia(100, 120.0, "UMIDO", 1),
            new TrechoRodovia(200, 150.0, "SECO", 2),
            new TrechoRodovia(300, 110.0, "UMIDO", 1)
        );
        
        when(trechoRepository.findAll()).thenReturn(trechosUrgentes);
        when(relatorioRepository.save(any(RelatorioPrioridade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RelatorioPrioridade resultado = relatorioService.gerarRelatorio();

        assertEquals(3, resultado.getUrgentes());
        assertEquals(0, resultado.getCriticos());
        assertEquals(0, resultado.getAtencao());
        assertEquals(0, resultado.getNormais());
    }
}
