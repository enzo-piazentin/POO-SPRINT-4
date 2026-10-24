package br.com.motiva.controller;

import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.service.RelatorioPrioridadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioPrioridadeController {

    @Autowired
    private RelatorioPrioridadeService relatorioService;

    @PostMapping
    public ResponseEntity<RelatorioPrioridade> gerarRelatorio() {
        RelatorioPrioridade relatorio = relatorioService.gerarRelatorio();
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorio);
    }

    @GetMapping
    public ResponseEntity<List<RelatorioPrioridade>> listarHistorico() {
        List<RelatorioPrioridade> relatorios = relatorioService.listarTodos();
        return ResponseEntity.ok(relatorios);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<RelatorioPrioridade>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<RelatorioPrioridade> relatorios = relatorioService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(relatorios);
    }
}
