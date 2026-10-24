package br.com.motiva.service;

import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.repository.EquipeManutencaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipeManutencaoService {

    @Autowired
    private EquipeManutencaoRepository equipeRepository;

    public List<EquipeManutencao> listarTodos() {
        return equipeRepository.findAll();
    }

    public Optional<EquipeManutencao> buscarPorId(Integer id) {
        return equipeRepository.findById(id);
    }

    public EquipeManutencao criar(EquipeManutencao equipe) {
        if (equipe.getNome() == null || equipe.getNome().isBlank()) {
            throw new IllegalArgumentException("nome da equipe é obrigatório");
        }
        if (equipe.getEspecialidade() == null || equipe.getEspecialidade().isBlank()) {
            throw new IllegalArgumentException("especialidade é obrigatória");
        }
        return equipeRepository.save(equipe);
    }

    public EquipeManutencao atualizar(Integer id, EquipeManutencao equipeAtualizada) {
        Optional<EquipeManutencao> equipeExistente = equipeRepository.findById(id);
        if (equipeExistente.isEmpty()) {
            throw new RuntimeException("Equipe não encontrada com ID: " + id);
        }

        EquipeManutencao equipe = equipeExistente.get();
        
        if (equipeAtualizada.getNome() == null || equipeAtualizada.getNome().isBlank()) {
            throw new IllegalArgumentException("nome da equipe é obrigatório");
        }
        if (equipeAtualizada.getEspecialidade() == null || equipeAtualizada.getEspecialidade().isBlank()) {
            throw new IllegalArgumentException("especialidade é obrigatória");
        }
        
        equipe.setNome(equipeAtualizada.getNome());
        equipe.setEspecialidade(equipeAtualizada.getEspecialidade());
        equipe.setDirecao(equipeAtualizada.getDirecao());
        
        return equipeRepository.save(equipe);
    }

    public void deletar(Integer id) {
        if (!equipeRepository.existsById(id)) {
            throw new RuntimeException("Equipe não encontrada com ID: " + id);
        }
        equipeRepository.deleteById(id);
    }

    public List<EquipeManutencao> buscarPorEspecialidade(String especialidade) {
        return equipeRepository.findByEspecialidade(especialidade);
    }

    public List<EquipeManutencao> buscarPorDirecao(String direcao) {
        return equipeRepository.findByDirecao(direcao);
    }
}
