# Proyecto: `block5-crud-validation`

## Descripción
Aplicación basada en Spring Boot que implementa un CRUD completo para gestionar la tabla `Persona`. El proyecto utiliza validaciones, mapeo entre entidades y DTOs, y manejo global de excepciones. El controlador trabaja exclusivamente con objetos `PersonaDto`, mientras que la lógica de negocio convierte estos DTOs en entidades para interactuar con la base de datos.

## Funcionalidades
- CRUD completo:
    - Crear people.
    - Leer people por ID, usuario o listar todas.
    - Actualizar people.
    - Eliminar people.
- Validaciones mediante `jakarta.validation`.
- Manejo de excepciones global con `@ControllerAdvice`.
- Uso de DTOs para aislar la lógica del controlador de las entidades.
- Mapeo entre DTOs y entidades mediante un `PersonaMapper`.
- Persistencia gestionada con `Spring Data JPA`.

## Estructura del Proyecto
| **Capa**             | **Descripción**                                                                                  |
|-----------------------|--------------------------------------------------------------------------------------------------|
| **Controlador**       | `PersonaController`: Define los endpoints REST y maneja las solicitudes con `PersonaDto`.       |
| **Servicio**          | `PersonaService`: Contiene la lógica de negocio, mapea DTOs a entidades y viceversa.            |
| **Repositorio**       | `PersonaRepository`: Interfaz que extiende `JpaRepository` para interactuar con la base de datos.|
| **Entidad**           | `Persona`: Representa la tabla `person` en la base de datos.                                   |
| **DTO**               | `PersonaDto`: Objeto de transferencia de datos utilizado en los controladores.                  |
| **Mapper**            | `PersonaMapper`: Mapea entre `Persona` y `PersonaDto`.                                          |
| **Excepciones**       | `GlobalExceptionHandler`: Maneja excepciones como `IllegalArgumentException` o genéricas.       |

## Endpoints
| **Método** | **Ruta**             | **Descripción**                               |
|------------|----------------------|-----------------------------------------------|
| `POST`     | `/person`           | Crear una nueva person.                     |
| `GET`      | `/person/{id}`      | Obtener person por ID.                      |
| `GET`      | `/person/usuario/{usuario}` | Obtener people por usuario.          |
| `GET`      | `/person`           | Listar todas las people.                   |
| `PUT`      | `/person/{id}`      | Actualizar una person existente.            |
| `DELETE`   | `/person/{id}`      | Eliminar una person por ID.                 |
- Probar los Endpoints con `Postman` en la carpeta resoures con el archivo `CrudValidationParte1.postman_collection.json`.

