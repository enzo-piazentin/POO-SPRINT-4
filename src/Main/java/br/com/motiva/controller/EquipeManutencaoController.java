package br.com.motiva.controller;

import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.service.EquipeManutencaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipes")
public class EquipeManutencaoController {

    @Autowired
    private EquipeManutencaoService equipeService;

    @GetMapping
    public ResponseEntity<List<EquipeManutencao>> listarTodos() {
        List<EquipeManutencao> equipes = equipeService.listarTodos();
        return ResponseEntity.ok(equipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeManutencao> buscarPorId(@PathVariable Integer id) {
        Optional<EquipeManutencao> equipe = equipeService.buscarPorId(id);
        return equipe.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EquipeManutencao> criar(@Valid @RequestBody EquipeManutencao equipe) {
        EquipeManutencao equipeCriada = equipeService.criar(equipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipeCriada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeManutencao> atualizar(@PathVariable Integer id, 
                                                      @Valid @RequestBody EquipeManutencao equipeAtualizada) {
        EquipeManutencao equipe = equipeService.atualizar(id, equipeAtualizada);
        return ResponseEntity.ok(equipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        equipeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<EquipeManutencao>> buscarPorEspecialidade(@PathVariable String especialidade) {
        List<EquipeManutencao> equipes = equipeService.buscarPorEspecialidade(especialidade);
        return ResponseEntity.ok(equipes);
    }

    @GetMapping("/direcao/{direcao}")
    public ResponseEntity<List<EquipeManutencao>> buscarPorDirecao(@PathVariable String direcao) {
        List<EquipeManutencao> equipes = equipeService.buscarPorDirecao(direcao);
        return ResponseEntity.ok(equipes);
    }
}
