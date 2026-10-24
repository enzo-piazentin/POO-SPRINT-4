package br.com.motiva.repository;

import br.com.motiva.model.TrechoRodovia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrechoRodoviaRepository extends JpaRepository<TrechoRodovia, Integer> {

    List<TrechoRodovia> findByTipoTerreno(String tipoTerreno);

    List<TrechoRodovia> findByAlturaVegetacaoGreaterThanEqual(Double minimo);

    List<TrechoRodovia> findByAlturaVegetacaoBetween(Double minimo, Double maximo);

    List<TrechoRodovia> findByEquipeId(Integer equipeId);

    Optional<TrechoRodovia> findByKm(Integer km);
}
