package br.com.motiva.repository;

import br.com.motiva.model.EquipeManutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipeManutencaoRepository extends JpaRepository<EquipeManutencao, Integer> {

    List<EquipeManutencao> findByEspecialidade(String especialidade);

    List<EquipeManutencao> findByDirecao(String direcao);

    Optional<EquipeManutencao> findByNome(String nome);
}
