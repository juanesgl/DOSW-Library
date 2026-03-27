# DOSW Library System 📚

Sistema de gestión de biblioteca desarrollado con **Spring Boot**, diseñado para gestionar libros, usuarios y préstamos de manera eficiente, siguiendo principios de desarrollo guiado por pruebas.

## 🚀 Características

- **Seguridad Avanzada**:
  - Autenticación y Autorización mediante **JWT (JSON Web Tokens)** Stateless.
  - Control de Acceso Basado en Roles (RBAC) con perfiles `USER` y `LIBRARIAN`.
  - Conexión segura obligatoria vía **HTTPS/SSL** (TLS) con certificado PKCS12.
- **Gestión de Usuarios**: Registro, encriptación de contraseñas y consulta de usuarios.
- **Inventario de Libros**: Control de stock, adición de libros y consulta de catálogo.
- **Sistema de Préstamos**: 
  - Préstamo de libros a usuarios.
  - Devolución de libros con actualización automática de stock.
  - Extracción de préstamos personales para el usuario autenticado (`/loans/my`).
  - Límite de 3 préstamos activos por usuario y validación de disponibilidad.
- **Protección de Entrada**: Validaciones estrictas de datos de entrada en todos los DTOs mediante anotaciones de Spring Validation, con respuestas de error estructuradas (400 Bad Request).
- **Documentación Interactiva**: API documentada con Swagger/OpenAPI (con soporte para Bearer Token Auth).
- **Cobertura de Pruebas**: Suite completa de pruebas unitarias y de integración de seguridad con reportes de JaCoCo. Ver [Análisis de Cobertura](src/test/README.md).

## 🛠️ Tecnologías y Herramientas

- **Java 21**: Lenguaje de programación.
- **Spring Boot 3.4.3**: Framework principal.
- **Spring Security & JJWT**: Seguridad integral, filtros y generación de tokens JWT.
- **Spring Data JPA**: Capa de persistencia y mapeo objeto-relacional (ORM).
- **Spring Validation**: Reglas estrictas de validación de datos en controladores y DTOs (`@Valid`, `@NotNull`, `@NotBlank`).
- **Maven**: Gestión de dependencias y construcción.
- **Lombok**: Reducción de código repetitivo.
- **JUnit 5 & Mockito**: Pruebas unitarias, mocks y `MockMvc` para integración web.
- **SpringDoc OpenAPI (Swagger)**: Documentación de la API segura.
- **JaCoCo**: Reporte de cobertura de código.
- **SonarQube**: Análisis estático
- **PostgreSQL**: Base de datos relacional.
- **Docker**: Contenedor para trabajo en Windows y Linux.

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
La aplicación estará disponible de forma segura en `https://localhost:8443` *(Nota: Debido al certificado autofirmado local, el navegador mostrará una advertencia de seguridad que debe aceptarse).*

## 📖 Documentación de la API

Una vez iniciada la aplicación, puedes acceder a la documentación interactiva en:
- **Swagger UI**: [https://localhost:8443/swagger-ui/index.html](https://localhost:8443/swagger-ui/index.html)
- **OpenAPI JSON**: [https://localhost:8443/v3/api-docs](https://localhost:8443/v3/api-docs)

> **Importante**: Para usar los endpoints protegidos, primero debes autenticarte en `/auth/login` con tus credenciales. Luego debes copiar el `token` devuelto y pegarlo dando clic en el botón verde **`Authorize 🔓`** en la parte superior derecha de Swagger.

### Endpoints Principales:
- `POST /auth/login`: Autenticación y obtención de JWT `(Público)`.
- `GET /users`: Listar usuarios `(Solo LIBRARIAN)`.
- `POST /users`: Registrar usuario inicial `(Solo LIBRARIAN)`.
- `GET /books`: Ver catálogo e inventario `(USER, LIBRARIAN)`.
- `POST /books`: Agregar libro al inventario `(Solo LIBRARIAN)`.
- `POST /loans?userId={id}&bookId={id}`: Crear un préstamo `(USER, LIBRARIAN)`.
- `PUT /loans/return?userId={id}&bookId={id}`: Devolver un libro `(USER, LIBRARIAN)`.
- `GET /loans/my`: Ver préstamos activos del usuario autenticado `(USER, LIBRARIAN)`.

## 🧪 Pruebas y Calidad

El proyecto cuenta con una cobertura exhaustiva en todas sus capas:

### Ejecutar Pruebas
```bash
mvn clean test
```

### Reporte de Cobertura (JaCoCo)
Después de ejecutar las pruebas, el reporte se genera en:
`target/site/jacoco/index.html`

### Estructura de Pruebas:
- **Core Services**: Lógica de negocio y reglas de validación.
- **Controllers**: Pruebas de integración web con `MockMvc`.
- **Security Integration Tests**: Verificación estricta de filtros JWT, respuestas `401 Unauthorized` por tokens faltantes/falsos y `403 Forbidden` por restricción de roles.
- **Mappers & Validators**: Validación de datos y transformación de DTOs.

### Videos demostrativos: 

Ver los videos para cada parte de la biblioteca [Videos explicativos](docs/videos.md)

## 📁 Estructura del Proyecto

Ver [Documentación de Arquitectura](src/main/README.md) para más detalles.

```text
src/main/java/edu/eci/dosw/tdd/
├── config/             # Configuraciones de Spring, CORS, HTTPS, Swagger y Security (JWT)
├── controller/         # Endpoints REST, manejador global de errores (Validaciones)
│   ├── dto/            # Data Transfer Objects asegurados con @NotBlank/@NotNull
│   └── mapper/         # Mapeos que excluyen hashes de contraseña en respuestas
├── core/               # Lógica de negocio (Dominio)
│   ├── exception/      # Excepciones personalizadas
│   ├── model/          # Entidades (Incluyendo contraseñas encriptadas)
│   ├── service/        # Servicios core
│   ├── util/           # Utilidades
│   └── validator/      # Validadores de negocio
├── persistence/        # Repositorios JPA
└── DoswLibraryApplication.java
```

---
*Este proyecto fue desarrollado como ejercicio de clase para la asignatura de Desarrollo de Operaciones de Software (DOSW).*
