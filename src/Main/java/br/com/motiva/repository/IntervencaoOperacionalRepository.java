package br.com.motiva.repository;

import br.com.motiva.model.IntervencaoOperacional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IntervencaoOperacionalRepository extends JpaRepository<IntervencaoOperacional, Integer> {

    List<IntervencaoOperacional> findByTipo(String tipo);

    List<IntervencaoOperacional> findByResponsavel(String responsavel);

    List<IntervencaoOperacional> findByKmTrecho(Integer kmTrecho);

    List<IntervencaoOperacional> findByDataIntervencaoBetween(LocalDate inicio, LocalDate fim);

    List<IntervencaoOperacional> findByKmTrechoAndTipo(Integer kmTrecho, String tipo);
}
