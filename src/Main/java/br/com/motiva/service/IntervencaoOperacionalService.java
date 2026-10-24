package br.com.motiva.service;

import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.repository.IntervencaoOperacionalRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IntervencaoOperacionalService {

    @Autowired
    private IntervencaoOperacionalRepository intervencaoRepository;

    @Autowired
    private TrechoRodoviaRepository trechoRepository;

    public List<IntervencaoOperacional> listarTodos() {
        return intervencaoRepository.findAll();
    }

    public Optional<IntervencaoOperacional> buscarPorId(Integer id) {
        return intervencaoRepository.findById(id);
    }

    public IntervencaoOperacional criar(IntervencaoOperacional intervencao) {
        if (intervencao.getTipo() == null || intervencao.getTipo().isBlank()) {
            throw new IllegalArgumentException("tipo da intervenção é obrigatório");
        }
        if (intervencao.getResponsavel() == null || intervencao.getResponsavel().isBlank()) {
            throw new IllegalArgumentException("responsável é obrigatório");
        }
        if (intervencao.getKmTrecho() == null) {
            throw new IllegalArgumentException("km do trecho é obrigatório");
        }
        
        if (!trechoRepository.existsById(intervencao.getKmTrecho())) {
            throw new RuntimeException("Trecho não encontrado com KM: " + intervencao.getKmTrecho());
        }
        
        return intervencaoRepository.save(intervencao);
    }

    public IntervencaoOperacional atualizar(Integer id, IntervencaoOperacional intervencaoAtualizada) {
        Optional<IntervencaoOperacional> intervencaoExistente = intervencaoRepository.findById(id);
        if (intervencaoExistente.isEmpty()) {
            throw new RuntimeException("Intervenção não encontrada com ID: " + id);
        }

        IntervencaoOperacional intervencao = intervencaoExistente.get();
        
        if (intervencaoAtualizada.getTipo() == null || intervencaoAtualizada.getTipo().isBlank()) {
            throw new IllegalArgumentException("tipo da intervenção é obrigatório");
        }
        if (intervencaoAtualizada.getResponsavel() == null || intervencaoAtualizada.getResponsavel().isBlank()) {
            throw new IllegalArgumentException("responsável é obrigatório");
        }
        
        intervencao.setTipo(intervencaoAtualizada.getTipo());
        intervencao.setResponsavel(intervencaoAtualizada.getResponsavel());
        intervencao.setDataIntervencao(intervencaoAtualizada.getDataIntervencao());
        
        return intervencaoRepository.save(intervencao);
    }

    public void deletar(Integer id) {
        if (!intervencaoRepository.existsById(id)) {
            throw new RuntimeException("Intervenção não encontrada com ID: " + id);
        }
        intervencaoRepository.deleteById(id);
    }

    public List<IntervencaoOperacional> buscarPorTipo(String tipo) {
        return intervencaoRepository.findByTipo(tipo);
    }

    public List<IntervencaoOperacional> buscarPorResponsavel(String responsavel) {
        return intervencaoRepository.findByResponsavel(responsavel);
    }

    public List<IntervencaoOperacional> buscarPorKmTrecho(Integer kmTrecho) {
        return intervencaoRepository.findByKmTrecho(kmTrecho);
    }
}
