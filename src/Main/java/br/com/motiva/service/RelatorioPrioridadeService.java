package br.com.motiva.service;

import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.RelatorioPrioridadeRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RelatorioPrioridadeService {

    @Autowired
    private RelatorioPrioridadeRepository relatorioRepository;

    @Autowired
    private TrechoRodoviaRepository trechoRepository;

    public List<RelatorioPrioridade> listarTodos() {
        return relatorioRepository.findAllByOrderByGeradoEmDesc();
    }

    public RelatorioPrioridade gerarRelatorio() {
        List<TrechoRodovia> trechos = trechoRepository.findAll();
        
        int urgente = 0, critico = 0, atencao = 0, normal = 0;
        StringBuilder resumo = new StringBuilder();
        
        resumo.append("=== RELATÓRIO DE PRIORIDADE ===\n");
        resumo.append("Gerado em: ").append(LocalDateTime.now()).append("\n\n");
        
        for (TrechoRodovia trecho : trechos) {
            double altura = trecho.getAlturaVegetacao();
            String prioridade = classificarPrioridade(altura);
            
            resumo.append("KM ").append(trecho.getKm())
                  .append(" - Altura: ").append(String.format("%.2f", altura))
                  .append(" cm - Prioridade: ").append(prioridade)
                  .append(" (").append(trecho.getTipoTerreno()).append(")\n");
            
            switch (prioridade) {
                case "URGENTE" -> urgente++;
                case "CRÍTICO" -> critico++;
                case "ATENÇÃO" -> atencao++;
                case "NORMAL" -> normal++;
            }
        }
        
        resumo.append("\n=== RESUMO ===\n");
        resumo.append("Urgentes: ").append(urgente).append("\n");
        resumo.append("Críticos: ").append(critico).append("\n");
        resumo.append("Atenção: ").append(atencao).append("\n");
        resumo.append("Normais: ").append(normal).append("\n");
        
        RelatorioPrioridade relatorio = new RelatorioPrioridade(
            urgente, critico, atencao, normal, resumo.toString()
        );
        
        return relatorioRepository.save(relatorio);
    }

    public List<RelatorioPrioridade> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return relatorioRepository.findByGeradoEmBetween(inicio, fim);
    }

    private String classificarPrioridade(double alturaVegetacao) {
        if (alturaVegetacao >= 100) {
            return "URGENTE";
        } else if (alturaVegetacao >= 60) {
            return "CRÍTICO";
        } else if (alturaVegetacao >= 30) {
            return "ATENÇÃO";
        } else {
            return "NORMAL";
        }
    }
}
