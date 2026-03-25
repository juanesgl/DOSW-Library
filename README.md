# DOSW Library System 📚

Sistema de gestión de biblioteca desarrollado con **Spring Boot**, diseñado para gestionar libros, usuarios y préstamos de manera eficiente, siguiendo principios de desarrollo guiado por pruebas.

## 🚀 Características

- **Gestión de Usuarios**: Registro y consulta de usuarios.
- **Inventario de Libros**: Control de stock, adición de libros y consulta de catálogo.
- **Sistema de Préstamos**: 
  - Préstamo de libros a usuarios.
  - Devolución de libros con actualización automática de stock.
  - Límite de 3 préstamos activos por usuario.
  - Validación de disponibilidad de libros.
- **Documentación Interactiva**: API documentada con Swagger/OpenAPI.
- **Cobertura de Pruebas**: Suite completa de pruebas unitarias e integración con reportes de JaCoCo y SonarQube. Ver [Análisis de Cobertura](src/test/README.md).

## 🛠️ Tecnologías y Herramientas

- **Java 21**: Lenguaje de programación.
- **Spring Boot 3.4.3**: Framework principal.
- **Maven**: Gestión de dependencias y construcción.
- **Lombok**: Reducción de código repetitivo.
- **JUnit 5 & Mockito**: Pruebas unitarias y mocks.
- **SpringDoc OpenAPI (Swagger)**: Documentación de la API.
- **JaCoCo**: Reporte de cobertura de código.
- **SonarQube**: Análisis estático

## 📋 Requisitos Previos

- Java JDK 21 o superior.
- Maven 3.8 o superior.

## ⚙️ Instalación y Ejecución

1. Clonar el repositorio.
2. Construir el proyecto:
   ```bash
   mvn clean install
   ```
3. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run
   ```
La aplicación estará disponible en `http://localhost:8080`.

## 📖 Documentación de la API

Una vez iniciada la aplicación, puedes acceder a la documentación interactiva en:

- **DIAGRAM VIEW**
```mermaiderDiagram
    USERS {
        Long id PK
        String name
        String username
        String password
        String role
    }
    BOOKS {
        Long id PK
        String title
        String author
        Integer total_quantity
        Integer available_quantity
    }
    LOANS {
        Long id PK
        Long book_id FK
        Long user_id FK
        Date loan_date
        Date return_date
        String status
    }
    USERS ||--o{ LOANS : "has"
    BOOKS ||--o{ LOANS : "is part of"
```
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Endpoints Principales:
- `GET /users`: Listar usuarios.
- `POST /users`: Registrar usuario.
- `GET /books`: Ver catálogo e inventario.
- `POST /books`: Agregar libro al inventario.
- `POST /loans?userId={id}&bookId={id}`: Crear un préstamo.
- `PUT /loans/return?userId={id}&bookId={id}`: Devolver un libro.

## 🧪 Pruebas y Calidad

El proyecto cuenta con una cobertura exhaustiva en todas sus capas:

### Ejecutar Pruebas
```bash
mvn test
```

### Reporte de Cobertura (JaCoCo)
Después de ejecutar las pruebas, el reporte se genera en:
`target/site/jacoco/index.html`

### Estructura de Pruebas:
- **Core Services**: Lógica de negocio y reglas de validación.
- **Controllers**: Pruebas de integración web con `MockMvc`.
- **Mappers & Validators**: Validación de datos y transformación de DTOs.

## 📁 Estructura del Proyecto

Ver [Documentación de Arquitectura](src/main/README.md) para más detalles.

```text
src/main/java/edu/eci/dosw/tdd/
├── config/             # Configuraciones de Spring
├── controller/         # Endpoints REST y DTOs
│   ├── dto/
│   └── mapper/
├── core/               # Lógica de negocio (Dominio)
│   ├── exception/      # Excepciones personalizadas
│   ├── model/          # Entidades
│   ├── service/        # Servicios core
│   ├── util/           # Utilidades (ID Gen, Fechas)
│   └── validator/      # Validadores de negocio
└── DoswLibraryApplication.java
```

---
*Este proyecto fue desarrollado como ejercicio de clase para la asignatura de Desarrollo de Operaciones de Software (DOSW).*
