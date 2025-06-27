package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.respository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=62c15c0d";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional <Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    1- Buscar series
                    2- Buscar episodios
                    3- Mostrar series buscadas
                    4- Buscar series por titulo
                    5- Top 5 mejores Series
                    6- Buscar series por categoria
                    7- Buscar serie por evaluacion
                    8- Buscar episodios por titulo
                    9- Top 5 mejores episodios por serie
                   
                    
                    0- Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscarSeriePorTemporadasYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion ...");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }
    }


    private DatosSerie getDatosSerie(){
            System.out.println("Escribe el nombre de la série que deseas buscar");
            //Busca los datos generales de las series
            var nombreSerie = teclado.nextLine();
            var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
            //https://www.omdbapi.com/?t=game+of+thrones&apikey=4fc7c187
            DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
            return datos;
        }
            private void buscarEpisodioPorSerie(){
        mostrarSeriesBuscadas();
                System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios: ");
                var nombreSerie =  teclado.nextLine();

                Optional<Serie> serie = series.stream()
                        .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                        .findFirst();

                if (serie.isPresent()){
                    var serieEncontrada = serie.get();
                    //Busca los datos de todas las temporadas
                    List<DatosTemporada> temporadas = new ArrayList<>();

                    for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                        var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                        DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                        temporadas.add(datosTemporada);
                    }
                    temporadas.forEach(System.out::println);
                    List<Episodio> episodios = temporadas.stream()
                            .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                            .collect(Collectors.toList());

                    serieEncontrada.setEpisodios(episodios);
                    repositorio.save(serieEncontrada);

                }

        }
        private  void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
       // datosSeries.add(datos);
            System.out.println(datos);
        }
    private void mostrarSeriesBuscadas() {
       series = repositorio.findAll(); //traera la lista de la base de datos

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }
  private void  buscarSeriesPorTitulo(){
      System.out.println("Escribe el nombre de la serie que deseas buscar: ");
      var nombreSerie = teclado.nextLine();
       serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

      if (serieBuscada.isPresent()){
          System.out.println("La serie buscada es: " + serieBuscada.get());
      }else {
          System.out.println("Serie no encontrada");
      }

  }

  private void buscarTop5Series(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s ->
                System.out.println("Serie: " + s.getTitulo() + " - Evaluacion: " + s.getEvaluacion()) );
  }
  private void buscarSeriesPorCategoria(){
      System.out.println("Escribe el genero/ categoria que buscas: ");
      var genero = teclado.nextLine();
      var categoria = Categoria.fromEspanol(genero);
      List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
      System.out.println("Las series de la categoria " + genero);
      seriesPorCategoria.forEach(System.out::println);

  }
  private void buscarSeriePorTemporadasYEvaluacion(){
      System.out.println("¿Cuantas temporadas tiene tu serie ideal?");
      var totalTemporadas = teclado.nextInt();
      teclado.nextLine();
      System.out.println("¿Cuál seria la evaluacion minima que deberia tener?");
      var evaluacion = teclado.nextDouble();
      teclado.nextLine();
      List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
      System.out.println("***Series Filtradas***");
      filtroSeries.forEach(s ->
              System.out.println(s.getTitulo() + " - Total de Temporadas: "+ s.getTotalTemporadas() + " - Evaluación: " + s.getEvaluacion()));


  }
  private void buscarEpisodiosPorTitulo(){
      System.out.println("Escribe el nombre del episodios que deseas buscar: ");
      var nombreEpisodio = teclado.nextLine();
      List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
      episodiosEncontrados.forEach(e ->
              System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                      e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));
  }
private void buscarTop5Episodios(){
        buscarSeriesPorTitulo();
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie: %s - Temporada: %s - Episodio: %s  %s - Evaluación: %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),e.getNumeroEpisodio(), e.getTitulo(), e.getEvaluacion()));

            }
        }

}

        //Mostrar solo el titulo de los episodios para las temporadas
      //  for (int i = 0; i < datos.totalTemporadas(); i++) {
        //    List<DatosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
          //  for (int j = 0; j < episodiosTemporadas.size(); j++) {
            //    System.out.println(episodiosTemporadas.get(j).titulo());
            //}

        // Mejoría usando funciones Lambda
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

      //Convertir todas las informaciones a una lista del tipo DatosEpisodios

      //  List<DatosEpisodio> datosEpisodios = temporadas.stream()
        //     .flatMap(t -> t.episodios().stream())
          //      .collect(Collectors.toList());

                //Mostrar Top 5 episodios
       // System.out.println("los mejores 5 episodios");
        //datosEpisodios.stream()
          //      .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A")) //ignora todo lo que tenga N/A en la api
              //  .peek((e -> System.out.println("Primer filtro (N/A" + e)))
              //  .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
               // .peek(e -> System.out.println("Segundo filtro, ordenacion (M>m)"+e)) //ordena de mayor a menor M>m
               // .map(e-> e.titulo().toUpperCase())
                //.peek(e -> System.out.println("Tercer filtro, Mayuscula (m>M)"+e))
                //.limit(5)
                //.forEach(System.out::println);

        //Convirtiendo los datos a una lista de tipo episodio
      //  List<Episodio> episodios = temporadas.stream() //Episodio declara lo que contiene la lista, episodios es el conjunto de lo que contiene
        //        .flatMap(t -> t.episodios().stream()
          //      .map(d -> new Episodio(t.numero(),d)))
            //    .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        //Busqueda de episodios apartir de una fecha (año)
      //  System.out.println("Por favor indica el año de los episodios que deseas ver");
        //var fecha = teclado.nextInt();
        //teclado.nextLine();

       // LocalDate fechaBusqueda = LocalDate.of(fecha, 1,1);

       // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // se puede modificar al gusto, crea un fromato para la fecha. puede ser MM/yyyy

      //  episodios.stream()
        //        .filter(e -> e.getFechaDeLanzamiento() != null &&  e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
          //      .forEach(e-> {
            //        System.out.println(
              //              "Temporada " + e.getTemporada() +
                //                    " Titulo " + e.getTitulo() +
                  //                  " Fecha de Lanzamiento " + e.getFechaDeLanzamiento().format(dtf)
                   // );
                //});
        //busca episodio por pedazo del titulo
       // System.out.println("Por favor escriba el titulo del episodio que desea ver");
        //var pedazoTitulo = teclado.nextLine();
        //Optional<Episodio> episodioBuscado = episodios.stream()
          //      .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
            //    .findFirst();
        //if (episodioBuscado.isPresent()) {
          //  System.out.println("Episodio encontrado");
            //System.out.println("Los datos son: "+ episodioBuscado.get());
        //}else {
          //  System.out.println("Episodio no encontrado");
       // }
       // Map<Integer, Double> evaluaconesPorTemporada = episodios.stream()
         //       .filter(e -> e.getEvaluacion() > 0.0)
           //     .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getEvaluacion)));
        //System.out.println(evaluaconesPorTemporada);

        //DoubleSummaryStatistics est = episodios.stream()
          //      .filter(e -> e.getEvaluacion() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));

  //      System.out.println("Media de las evaluaciones: " + est.getAverage());
    //    System.out.println("Episodio mejor evaluado: " + est.getMax());
      //  System.out.println("Episodio peor evaluado: " + est.getMin());

    //}

//}









//String texto = "Ejemplo";  Ejercicios...
// System.out.println("-----------------------------");
// String mayus = texto.toUpperCase();
// System.out.println("----------------------------");
// String vowel = mayus.replace("E", "O");
//
// System.out.println(texto+ " "
//       + " " + mayus +
//       " " + vowel);
//List<String> ejemplo = Arrays.asList(vowel);

//ejemplo.stream().forEach(System.out::println);