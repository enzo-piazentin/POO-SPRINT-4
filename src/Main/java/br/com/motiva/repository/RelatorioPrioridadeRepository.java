package br.com.motiva.repository;

import br.com.motiva.model.RelatorioPrioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RelatorioPrioridadeRepository extends JpaRepository<RelatorioPrioridade, Integer> {

    List<RelatorioPrioridade> findByGeradoEmBetween(LocalDateTime inicio, LocalDateTime fim);

    List<RelatorioPrioridade> findAllByOrderByGeradoEmDesc();
}
