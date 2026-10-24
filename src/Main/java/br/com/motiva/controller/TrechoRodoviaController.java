package br.com.motiva.controller;

import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.service.TrechoRodoviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trechos")
public class TrechoRodoviaController {

    @Autowired
    private TrechoRodoviaService trechoService;

    @GetMapping
    public ResponseEntity<List<TrechoRodovia>> listarTodos() {
        List<TrechoRodovia> trechos = trechoService.listarTodos();
        return ResponseEntity.ok(trechos);
    }

    @GetMapping("/{km}")
    public ResponseEntity<TrechoRodovia> buscarPorKm(@PathVariable Integer km) {
        Optional<TrechoRodovia> trecho = trechoService.buscarPorKm(km);
        return trecho.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TrechoRodovia> criar(@Valid @RequestBody TrechoRodovia trecho) {
        TrechoRodovia trechoCriado = trechoService.criar(trecho);
        return ResponseEntity.status(HttpStatus.CREATED).body(trechoCriado);
    }

    @PutMapping("/{km}")
    public ResponseEntity<TrechoRodovia> atualizar(@PathVariable Integer km, 
                                                   @Valid @RequestBody TrechoRodovia trechoAtualizado) {
        TrechoRodovia trecho = trechoService.atualizar(km, trechoAtualizado);
        return ResponseEntity.ok(trecho);
    }

    @DeleteMapping("/{km}")
    public ResponseEntity<Void> deletar(@PathVariable Integer km) {
        trechoService.deletar(km);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo/{tipoTerreno}")
    public ResponseEntity<List<TrechoRodovia>> buscarPorTipoTerreno(@PathVariable String tipoTerreno) {
        List<TrechoRodovia> trechos = trechoService.buscarPorTipoTerreno(tipoTerreno);
        return ResponseEntity.ok(trechos);
    }

    @GetMapping("/altura-minima/{minimo}")
    public ResponseEntity<List<TrechoRodovia>> buscarPorAlturaMinima(@PathVariable Double minimo) {
        List<TrechoRodovia> trechos = trechoService.buscarPorAlturaMinima(minimo);
        return ResponseEntity.ok(trechos);
    }
}
