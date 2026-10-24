package br.com.motiva.service;

import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.repository.EquipeManutencaoRepository;
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
class EquipeManutencaoServiceTest {

    @Mock
    private EquipeManutencaoRepository equipeRepository;

    @InjectMocks
    private EquipeManutencaoService equipeService;

    private EquipeManutencao equipe;

    @BeforeEach
    void setUp() {
        equipe = new EquipeManutencao(1, "Equipe Leste", "ROCADA", "LESTE");
    }

    @Test
    void listarTodos_DeveRetornarListaDeEquipes() {
        List<EquipeManutencao> equipes = Arrays.asList(
            new EquipeManutencao(1, "Equipe Leste", "ROCADA", "LESTE"),
            new EquipeManutencao(2, "Equipe Norte", "PODA", "NORTE")
        );
        
        when(equipeRepository.findAll()).thenReturn(equipes);

        List<EquipeManutencao> resultado = equipeService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("Equipe Leste", resultado.get(0).getNome());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_QuandoEquipeExiste_DeveRetornarEquipe() {
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        Optional<EquipeManutencao> resultado = equipeService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Equipe Leste", resultado.get().getNome());
        verify(equipeRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_QuandoEquipeNaoExiste_DeveRetornarVazio() {
        when(equipeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<EquipeManutencao> resultado = equipeService.buscarPorId(999);

        assertFalse(resultado.isPresent());
        verify(equipeRepository, times(1)).findById(999);
    }

    @Test
    void criar_QuandoDadosValidos_DeveCriarEquipe() {
        when(equipeRepository.save(any(EquipeManutencao.class))).thenReturn(equipe);

        EquipeManutencao resultado = equipeService.criar(equipe);

        assertNotNull(resultado);
        assertEquals("Equipe Leste", resultado.getNome());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void criar_QuandoNomeVazio_DeveLancarExcecao() {
        EquipeManutencao equipeInvalida = new EquipeManutencao(1, "", "ROCADA", "LESTE");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> equipeService.criar(equipeInvalida)
        );

        assertEquals("nome da equipe é obrigatório", exception.getMessage());
        verify(equipeRepository, never()).save(any());
    }

    @Test
    void criar_QuandoNomeNulo_DeveLancarExcecao() {
        EquipeManutencao equipeInvalida = new EquipeManutencao(1, null, "ROCADA", "LESTE");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> equipeService.criar(equipeInvalida)
        );

        assertEquals("nome da equipe é obrigatório", exception.getMessage());
        verify(equipeRepository, never()).save(any());
    }

    @Test
    void criar_QuandoEspecialidadeVazia_DeveLancarExcecao() {
        EquipeManutencao equipeInvalida = new EquipeManutencao(1, "Equipe Leste", "", "LESTE");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> equipeService.criar(equipeInvalida)
        );

        assertEquals("especialidade é obrigatória", exception.getMessage());
        verify(equipeRepository, never()).save(any());
    }

    @Test
    void atualizar_QuandoEquipeExiste_DeveAtualizarEquipe() {
        EquipeManutencao equipeAtualizada = new EquipeManutencao(1, "Equipe Oeste", "PODA", "OESTE");
        
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));
        when(equipeRepository.save(any(EquipeManutencao.class))).thenReturn(equipeAtualizada);

        EquipeManutencao resultado = equipeService.atualizar(1, equipeAtualizada);

        assertNotNull(resultado);
        assertEquals("Equipe Oeste", resultado.getNome());
        verify(equipeRepository, times(1)).findById(1);
        verify(equipeRepository, times(1)).save(any());
    }

    @Test
    void atualizar_QuandoEquipeNaoExiste_DeveLancarExcecao() {
        EquipeManutencao equipeAtualizada = new EquipeManutencao(999, "Equipe Oeste", "PODA", "OESTE");
        
        when(equipeRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> equipeService.atualizar(999, equipeAtualizada)
        );

        assertEquals("Equipe não encontrada com ID: 999", exception.getMessage());
        verify(equipeRepository, times(1)).findById(999);
        verify(equipeRepository, never()).save(any());
    }

    @Test
    void deletar_QuandoEquipeExiste_DeveDeletarEquipe() {
        when(equipeRepository.existsById(1)).thenReturn(true);
        doNothing().when(equipeRepository).deleteById(1);

        equipeService.deletar(1);

        verify(equipeRepository, times(1)).existsById(1);
        verify(equipeRepository, times(1)).deleteById(1);
    }

    @Test
    void deletar_QuandoEquipeNaoExiste_DeveLancarExcecao() {
        when(equipeRepository.existsById(999)).thenReturn(false);

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> equipeService.deletar(999)
        );

        assertEquals("Equipe não encontrada com ID: 999", exception.getMessage());
        verify(equipeRepository, times(1)).existsById(999);
        verify(equipeRepository, never()).deleteById(any());
    }

    @Test
    void buscarPorEspecialidade_DeveRetornarEquipesDaEspecialidade() {
        List<EquipeManutencao> equipes = Arrays.asList(
            new EquipeManutencao(1, "Equipe Leste", "ROCADA", "LESTE"),
            new EquipeManutencao(2, "Equipe Norte", "ROCADA", "NORTE")
        );
        
        when(equipeRepository.findByEspecialidade("ROCADA")).thenReturn(equipes);

        List<EquipeManutencao> resultado = equipeService.buscarPorEspecialidade("ROCADA");

        assertEquals(2, resultado.size());
        verify(equipeRepository, times(1)).findByEspecialidade("ROCADA");
    }

    @Test
    void buscarPorDirecao_DeveRetornarEquipesDaDirecao() {
        List<EquipeManutencao> equipes = Arrays.asList(
            new EquipeManutencao(1, "Equipe Leste", "ROCADA", "LESTE"),
            new EquipeManutencao(3, "Equipe Sudeste", "PODA", "LESTE")
        );
        
        when(equipeRepository.findByDirecao("LESTE")).thenReturn(equipes);

        List<EquipeManutencao> resultado = equipeService.buscarPorDirecao("LESTE");

        assertEquals(2, resultado.size());
        verify(equipeRepository, times(1)).findByDirecao("LESTE");
    }
}
