package br.com.motiva.service;

import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrechoRodoviaService {

    @Autowired
    private TrechoRodoviaRepository trechoRepository;

    public List<TrechoRodovia> listarTodos() {
        return trechoRepository.findAll();
    }

    public Optional<TrechoRodovia> buscarPorKm(Integer km) {
        return trechoRepository.findByKm(km);
    }

    public TrechoRodovia criar(TrechoRodovia trecho) {
        if (trecho.getKm() < 0) {
            throw new IllegalArgumentException("km não pode ser negativo");
        }
        if (trecho.getAlturaVegetacao() < 0) {
            throw new IllegalArgumentException("altura da vegetação não pode ser negativa");
        }
        if (trecho.getTipoTerreno() == null || trecho.getTipoTerreno().isBlank()) {
            throw new IllegalArgumentException("tipoTerreno é obrigatório");
        }
        return trechoRepository.save(trecho);
    }

    public TrechoRodovia atualizar(Integer km, TrechoRodovia trechoAtualizado) {
        Optional<TrechoRodovia> trechoExistente = trechoRepository.findByKm(km);
        if (trechoExistente.isEmpty()) {
            throw new RuntimeException("Trecho não encontrado com KM: " + km);
        }

        TrechoRodovia trecho = trechoExistente.get();
        
        if (trechoAtualizado.getAlturaVegetacao() < 0) {
            throw new IllegalArgumentException("altura da vegetação não pode ser negativa");
        }
        
        trecho.setAlturaVegetacao(trechoAtualizado.getAlturaVegetacao());
        trecho.setTipoTerreno(trechoAtualizado.getTipoTerreno());
        trecho.setEquipeId(trechoAtualizado.getEquipeId());
        
        return trechoRepository.save(trecho);
    }

    public void deletar(Integer km) {
        Optional<TrechoRodovia> trecho = trechoRepository.findByKm(km);
        if (trecho.isEmpty()) {
            throw new RuntimeException("Trecho não encontrado com KM: " + km);
        }
        trechoRepository.delete(trecho.get());
    }

    public List<TrechoRodovia> buscarPorTipoTerreno(String tipoTerreno) {
        return trechoRepository.findByTipoTerreno(tipoTerreno);
    }

    public List<TrechoRodovia> buscarPorAlturaMinima(Double minimo) {
        return trechoRepository.findByAlturaVegetacaoGreaterThanEqual(minimo);
    }

    public void atualizarVegetacaoTodos() {
        List<TrechoRodovia> trechos = trechoRepository.findAll();
        for (TrechoRodovia trecho : trechos) {
            trecho.atualizarVegetacao();
            trechoRepository.save(trecho);
        }
    }
}
