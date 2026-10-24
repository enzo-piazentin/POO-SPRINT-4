package br.com.motiva.controller;

import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.service.IntervencaoOperacionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/intervencoes")
public class IntervencaoOperacionalController {

    @Autowired
    private IntervencaoOperacionalService intervencaoService;

    @GetMapping
    public ResponseEntity<List<IntervencaoOperacional>> listarTodos() {
        List<IntervencaoOperacional> intervencoes = intervencaoService.listarTodos();
        return ResponseEntity.ok(intervencoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntervencaoOperacional> buscarPorId(@PathVariable Integer id) {
        Optional<IntervencaoOperacional> intervencao = intervencaoService.buscarPorId(id);
        return intervencao.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IntervencaoOperacional> criar(@Valid @RequestBody IntervencaoOperacional intervencao) {
        IntervencaoOperacional intervencaoCriada = intervencaoService.criar(intervencao);
        return ResponseEntity.status(HttpStatus.CREATED).body(intervencaoCriada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntervencaoOperacional> atualizar(@PathVariable Integer id,
                                                           @Valid @RequestBody IntervencaoOperacional intervencaoAtualizada) {
        IntervencaoOperacional intervencao = intervencaoService.atualizar(id, intervencaoAtualizada);
        return ResponseEntity.ok(intervencao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        intervencaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<IntervencaoOperacional>> buscarPorTipo(@PathVariable String tipo) {
        List<IntervencaoOperacional> intervencoes = intervencaoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(intervencoes);
    }

    @GetMapping("/responsavel/{responsavel}")
    public ResponseEntity<List<IntervencaoOperacional>> buscarPorResponsavel(@PathVariable String responsavel) {
        List<IntervencaoOperacional> intervencoes = intervencaoService.buscarPorResponsavel(responsavel);
        return ResponseEntity.ok(intervencoes);
    }

    @GetMapping("/trecho/{kmTrecho}")
    public ResponseEntity<List<IntervencaoOperacional>> buscarPorKmTrecho(@PathVariable Integer kmTrecho) {
        List<IntervencaoOperacional> intervencoes = intervencaoService.buscarPorKmTrecho(kmTrecho);
        return ResponseEntity.ok(intervencoes);
    }
}
