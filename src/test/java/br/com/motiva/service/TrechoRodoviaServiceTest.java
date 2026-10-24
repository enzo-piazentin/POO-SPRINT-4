package br.com.motiva.service;

import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrechoRodoviaServiceTest {

    @Mock
    private TrechoRodoviaRepository trechoRepository;

    @InjectMocks
    private TrechoRodoviaService trechoService;

    private TrechoRodovia trecho;

    @BeforeEach
    void setUp() {
        trecho = new TrechoRodovia(250, 75.0, "UMIDO", 1);
    }

    @Test
    void listarTodos_DeveRetornarListaDeTrechos() {
        List<TrechoRodovia> trechos = Arrays.asList(
            new TrechoRodovia(250, 75.0, "UMIDO", 1),
            new TrechoRodovia(300, 50.0, "SECO", 2)
        );
        
        when(trechoRepository.findAll()).thenReturn(trechos);

        List<TrechoRodovia> resultado = trechoService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(250, resultado.get(0).getKm());
        verify(trechoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorKm_QuandoTrechoExiste_DeveRetornarTrecho() {
        when(trechoRepository.findByKm(250)).thenReturn(Optional.of(trecho));

        Optional<TrechoRodovia> resultado = trechoService.buscarPorKm(250);

        assertTrue(resultado.isPresent());
        assertEquals(250, resultado.get().getKm());
        assertEquals(75.0, resultado.get().getAlturaVegetacao());
        verify(trechoRepository, times(1)).findByKm(250);
    }

    @Test
    void buscarPorKm_QuandoTrechoNaoExiste_DeveRetornarVazio() {
        when(trechoRepository.findByKm(999)).thenReturn(Optional.empty());

        Optional<TrechoRodovia> resultado = trechoService.buscarPorKm(999);

        assertFalse(resultado.isPresent());
        verify(trechoRepository, times(1)).findByKm(999);
    }

    @Test
    void criar_QuandoDadosValidos_DeveCriarTrecho() {
        when(trechoRepository.save(any(TrechoRodovia.class))).thenReturn(trecho);

        TrechoRodovia resultado = trechoService.criar(trecho);

        assertNotNull(resultado);
        assertEquals(250, resultado.getKm());
        assertEquals(75.0, resultado.getAlturaVegetacao());
        verify(trechoRepository, times(1)).save(trecho);
    }

    @Test
    void criar_QuandoKmNegativo_DeveLancarExcecao() {
        TrechoRodovia trechoInvalido = new TrechoRodovia();
        trechoInvalido.setKm(-10);
        trechoInvalido.setAlturaVegetacao(75.0);
        trechoInvalido.setTipoTerreno("UMIDO");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> trechoService.criar(trechoInvalido)
        );

        assertEquals("km não pode ser negativo", exception.getMessage());
        verify(trechoRepository, never()).save(any());
    }

    @Test
    void criar_QuandoAlturaVegetacaoNegativa_DeveLancarExcecao() {
        TrechoRodovia trechoInvalido = new TrechoRodovia();
        trechoInvalido.setKm(250);
        trechoInvalido.setAlturaVegetacao(-10.0);
        trechoInvalido.setTipoTerreno("UMIDO");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> trechoService.criar(trechoInvalido)
        );

        assertEquals("altura da vegetação não pode ser negativa", exception.getMessage());
        verify(trechoRepository, never()).save(any());
    }

    @Test
    void criar_QuandoTipoTerrenoNulo_DeveLancarExcecao() {
        TrechoRodovia trechoInvalido = new TrechoRodovia(250, 75.0, null, 1);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> trechoService.criar(trechoInvalido)
        );

        assertEquals("tipoTerreno é obrigatório", exception.getMessage());
        verify(trechoRepository, never()).save(any());
    }

    @Test
    void criar_QuandoTipoTerrenoVazio_DeveLancarExcecao() {
        TrechoRodovia trechoInvalido = new TrechoRodovia(250, 75.0, "", 1);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> trechoService.criar(trechoInvalido)
        );

        assertEquals("tipoTerreno é obrigatório", exception.getMessage());
        verify(trechoRepository, never()).save(any());
    }

    @Test
    void atualizar_QuandoTrechoExiste_DeveAtualizarTrecho() {
        TrechoRodovia trechoAtualizado = new TrechoRodovia(250, 30.0, "SECO", 2);
        
        when(trechoRepository.findByKm(250)).thenReturn(Optional.of(trecho));
        when(trechoRepository.save(any(TrechoRodovia.class))).thenReturn(trechoAtualizado);

        TrechoRodovia resultado = trechoService.atualizar(250, trechoAtualizado);

        assertNotNull(resultado);
        assertEquals(30.0, resultado.getAlturaVegetacao());
        assertEquals("SECO", resultado.getTipoTerreno());
        verify(trechoRepository, times(1)).findByKm(250);
        verify(trechoRepository, times(1)).save(any());
    }

    @Test
    void atualizar_QuandoTrechoNaoExiste_DeveLancarExcecao() {
        TrechoRodovia trechoAtualizado = new TrechoRodovia(999, 30.0, "SECO", 2);
        
        when(trechoRepository.findByKm(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> trechoService.atualizar(999, trechoAtualizado)
        );

        assertEquals("Trecho não encontrado com KM: 999", exception.getMessage());
        verify(trechoRepository, times(1)).findByKm(999);
        verify(trechoRepository, never()).save(any());
    }

    @Test
    void atualizar_QuandoAlturaVegetacaoNegativa_DeveLancarExcecao() {
        TrechoRodovia trechoAtualizado = new TrechoRodovia();
        trechoAtualizado.setAlturaVegetacao(-10.0);
        trechoAtualizado.setTipoTerreno("SECO");
        
        when(trechoRepository.findByKm(250)).thenReturn(Optional.of(trecho));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> trechoService.atualizar(250, trechoAtualizado)
        );

        assertEquals("altura da vegetação não pode ser negativa", exception.getMessage());
        verify(trechoRepository, times(1)).findByKm(250);
        verify(trechoRepository, never()).save(any());
    }

    @Test
    void deletar_QuandoTrechoExiste_DeveDeletarTrecho() {
        when(trechoRepository.findByKm(250)).thenReturn(Optional.of(trecho));
        doNothing().when(trechoRepository).delete(trecho);

        trechoService.deletar(250);

        verify(trechoRepository, times(1)).findByKm(250);
        verify(trechoRepository, times(1)).delete(trecho);
    }

    @Test
    void deletar_QuandoTrechoNaoExiste_DeveLancarExcecao() {
        when(trechoRepository.findByKm(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> trechoService.deletar(999)
        );

        assertEquals("Trecho não encontrado com KM: 999", exception.getMessage());
        verify(trechoRepository, times(1)).findByKm(999);
        verify(trechoRepository, never()).delete(any());
    }

    @Test
    void buscarPorTipoTerreno_DeveRetornarTrechosDoTipo() {
        List<TrechoRodovia> trechos = Arrays.asList(
            new TrechoRodovia(250, 75.0, "UMIDO", 1),
            new TrechoRodovia(300, 80.0, "UMIDO", 2)
        );
        
        when(trechoRepository.findByTipoTerreno("UMIDO")).thenReturn(trechos);

        List<TrechoRodovia> resultado = trechoService.buscarPorTipoTerreno("UMIDO");

        assertEquals(2, resultado.size());
        verify(trechoRepository, times(1)).findByTipoTerreno("UMIDO");
    }

    @Test
    void buscarPorAlturaMinima_DeveRetornarTrechosAcimaDoMinimo() {
        List<TrechoRodovia> trechos = Arrays.asList(
            new TrechoRodovia(250, 75.0, "UMIDO", 1),
            new TrechoRodovia(300, 80.0, "UMIDO", 2)
        );
        
        when(trechoRepository.findByAlturaVegetacaoGreaterThanEqual(50.0)).thenReturn(trechos);

        List<TrechoRodovia> resultado = trechoService.buscarPorAlturaMinima(50.0);

        assertEquals(2, resultado.size());
        verify(trechoRepository, times(1)).findByAlturaVegetacaoGreaterThanEqual(50.0);
    }

    @Test
    void atualizarVegetacaoTodos_DeveAtualizarTodosOsTrechos() {
        List<TrechoRodovia> trechos = Arrays.asList(
            new TrechoRodovia(250, 75.0, "UMIDO", 1),
            new TrechoRodovia(300, 50.0, "SECO", 2)
        );
        
        when(trechoRepository.findAll()).thenReturn(trechos);
        when(trechoRepository.save(any(TrechoRodovia.class))).thenReturn(trecho);

        trechoService.atualizarVegetacaoTodos();

        verify(trechoRepository, times(1)).findAll();
        verify(trechoRepository, times(2)).save(any(TrechoRodovia.class));
    }

    @Test
    void calcularCrescimentoDiario_TerrenoUmid_DeveRetornar5() {
        TrechoRodovia trechoUmid = new TrechoRodovia(250, 75.0, "UMIDO", 1);
        
        double crescimento = trechoUmid.calcularCrescimentoDiario();

        assertEquals(5.0, crescimento);
    }

    @Test
    void calcularCrescimentoDiario_TerrenoSeco_DeveRetornar2() {
        TrechoRodovia trechoSeco = new TrechoRodovia(250, 75.0, "SECO", 1);
        
        double crescimento = trechoSeco.calcularCrescimentoDiario();

        assertEquals(2.0, crescimento);
    }
}
