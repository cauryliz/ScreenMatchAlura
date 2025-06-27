package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Actors") String actores,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Plot") String sinopsis,
        @JsonAlias("totalSeasons")Integer totalTemporadas,
        @JsonAlias("imdbRating") String evaluacion) {
}
