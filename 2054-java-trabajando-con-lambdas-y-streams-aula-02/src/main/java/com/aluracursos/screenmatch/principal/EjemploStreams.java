package com.aluracursos.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Lizbeth", "Daniel","Nailah","Concepcion", "Jose");

        nombres.stream()
                .sorted() //ordena alfabeticamente
                //.limit(2) //limita la cantidad de elementos de la lista
               // .filter(n -> n.startsWith("L"))  //filtra y muestra solo lo que inicie con L
                //.map(n -> n.toUpperCase()) // convierte minusculas a mayusculas
               .forEach(System.out::println);
    }
}
