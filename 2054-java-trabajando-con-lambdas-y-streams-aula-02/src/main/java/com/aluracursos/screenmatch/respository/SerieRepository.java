package com.aluracursos.screenmatch.respository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface SerieRepository extends JpaRepository<Serie, Long> {
   Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

   List<Serie> findTop5ByOrderByEvaluacionDesc();//crea una lista ordenando de forma decreciente mayor a menor las evaluaciones de las series
   List<Serie> findByGenero(Categoria categoria);
   //List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas,Double evaluacion);
   @Query(value = "SELECT s FROM Serie s WHERE s.totalTemporadas < :totalTemporadas AND s.evaluacion > :evaluacion")
   List<Serie> seriesPorTemporadaYEvaluacion(int totalTemporadas, Double evaluacion);
   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")  //ILIKE es para buscar en una tabla, insensible a mayusculas y minusculas
   List<Episodio> episodiosPorNombre(String nombreEpisodio);

   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
   List<Episodio> top5Episodios(Serie serie);
}
