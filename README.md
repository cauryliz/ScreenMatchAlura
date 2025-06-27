# ScreenMatchAlura
# 🎬 ScreenMatch Application

**ScreenMatch Application** es una aplicación de consola desarrollada en Java con Spring Framework que permite buscar series, conectarse a una API externa, almacenar resultados en una base de datos y filtrar los mejores contenidos mediante consultas personalizadas.

---

## 🚀 Características principales

- 🔗 **Consumo de API externa** para obtener información de series.
- 🗃️ **Persistencia de datos** usando **Spring Data JPA** y **Hibernate**.
- 💻 **Interfaz de consola** para interacción con el usuario.
- 🔍 **Búsqueda y filtrado** de series según criterios como temporadas y evaluación.
- ⭐ **Muestra de los 5 mejores episodios** y **5 mejores series**.
- 🧠 Uso de **JPQL** para consultas avanzadas.
- 💡 Aplicación de **expresiones lambda** y manejo funcional de colecciones.

---

## 🛠️ Tecnologías utilizadas
      nb
- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Lambdas y Streams de Java**
- **API REST externa** (para obtener datos de series)
- **Base de datos relacional** (como H2, MySQL o PostgreSQL)

---

## 🧩 Estructura del proyecto

com.aluracursos.screenmatch
│
├── model -> Clases entidad (Serie, Episodio)
├── repository -> Interfaces JPA (SerieRepository, EpisodioRepository)
├── service -> Lógica de negocio (consumo API, filtrado, persistencia)
├── principal -> Clase principal con menú de consola
└── utils -> Clases utilitarias (conversores, etc.)


---

## ⚙️ Cómo ejecutar

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/screenmatch-application.git
   cd screenmatch-application
