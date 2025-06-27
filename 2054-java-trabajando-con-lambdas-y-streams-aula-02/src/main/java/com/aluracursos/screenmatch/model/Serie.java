package com.aluracursos.screenmatch.model;

import com.aluracursos.screenmatch.service.ConsultaGemini;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")

public class Serie {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
     @Column(unique = true)
    private String titulo;
    private String poster;
    private String actores;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String sinopsis;
    private Integer totalTemporadas;
    private Double evaluacion;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER) //mapeamos atraves del campo serie
    private List<Episodio> episodios;

    public Serie(){}

     public Serie(DatosSerie datosSerie){

     this.titulo = datosSerie.titulo();
     this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
     this.sinopsis = ConsultaGemini.obtenerTraduccion(datosSerie.sinopsis());
     this.actores = datosSerie.actores();
     this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
     this.totalTemporadas = datosSerie.totalTemporadas();
     this.poster = datosSerie.poster();

            }

 @Override
 public String toString() {
  return "titulo='" + titulo + '\'' +
          ", poster='" + poster + '\'' +
          ", actores='" + actores + '\'' +
          ", genero=" + genero +
          ", sinopsis='" + sinopsis + '\'' +
          ", totalTemporadas=" + totalTemporadas +
          ", evaluacion=" + evaluacion + '\'' +
          ", episodios=" + episodios
          ;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getTitulo() {
  return titulo;
 }

 public void setTitulo(String titulo) {
  this.titulo = titulo;
 }

 public String getPoster() {
  return poster;
 }

 public void setPoster(String poster) {
  this.poster = poster;
 }

 public String getActores() {
  return actores;
 }

 public void setActores(String actores) {
  this.actores = actores;
 }

 public Categoria getGenero() {
  return genero;
 }

 public void setGenero(Categoria genero) {
  this.genero = genero;
 }

 public String getSinopsis() {
  return sinopsis;
 }

 public void setSinopsis(String sinopsis) {
  this.sinopsis = sinopsis;
 }

 public Integer getTotalTemporadas() {
  return totalTemporadas;
 }

 public void setTotalTemporadas(Integer totalTemporadas) {
  this.totalTemporadas = totalTemporadas;
 }

 public Double getEvaluacion() {
  return evaluacion;
 }

 public void setEvaluacion(Double evaluacion) {
  this.evaluacion = evaluacion;
 }

 public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this)); //para cada episodio va a hacer set con el id de cada serie
      this.episodios = episodios;
  }

 }

