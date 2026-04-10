# DOSW Library System 📚

Sistema de gestión de biblioteca con **Spring Boot**, con libros, usuarios y préstamos, desarrollo guiado por pruebas (TDD) y API documentada.

## 🚀 Características

- **Seguridad**:
  - Autenticación con **JWT** (stateless).
  - RBAC con roles `USER` y `LIBRARIAN`.
  - **HTTPS opcional** (TLS con keystore PKCS12); en local o en la nube puedes usar HTTP si `SSL_ENABLED=false`.
- **Persistencia dual (perfiles Spring)**:
  - Perfil **`mongo`** (por defecto): **MongoDB** (por ejemplo Atlas); sin arranque de JDBC/Hibernate.
  - Perfil **`relational`**: **PostgreSQL** + **Spring Data JPA** / Hibernate.
- **Gestión de usuarios**: registro, contraseñas con BCrypt, consultas según rol.
- **Inventario de libros**: catálogo, stock y altas (bibliotecario).
- **Préstamos**:
  - Creación y devolución con actualización de disponibilidad.
  - `GET /loans/my` para el usuario autenticado.
  - Límite de préstamos activos y validaciones de negocio.
- **Validación**: DTOs con Spring Validation y respuestas de error coherentes (`GlobalExceptionHandler`).
- **Documentación**: **SpringDoc / OpenAPI** (Swagger UI) con soporte Bearer JWT.
- **Pruebas**: unitarias, integración con `MockMvc`, seguridad; **JaCoCo** para cobertura. Detalle en [Análisis de cobertura](src/test/README.md).
- **CI**: GitHub Actions (compilación, tests con `JWT_SECRET_KEY`).

## 🛠️ Tecnologías

| Área | Stack |
|------|--------|
| Runtime | Java **21**, Spring Boot **3.4.3** |
| Web / API | Spring Web, SpringDoc OpenAPI **2.8.4** |
| Seguridad | Spring Security, **jjwt** |
| NoSQL | Spring Data **MongoDB** |
| SQL | Spring Data **JPA**, Hibernate, **PostgreSQL** (perfil `relational`) |
| Tests | JUnit 5, Mockito, H2 en memoria (perfil `relational` en test) |
| Build | Maven, Lombok |

## 📋 Requisitos

- **JDK 21+**
- **Maven 3.8+**
- Según perfil:
  - **`mongo`**: URI de MongoDB (`MONGO_URI` o `SPRING_DATA_MONGODB_URI`).
  - **`relational`**: PostgreSQL accesible y variables `DB_URL`, `DB_USER`, `DB_PASSWORD`.

## ⚙️ Perfiles y configuración

| Perfil | Archivo principal | Base de datos |
|--------|-------------------|---------------|
| `mongo` *(default)* | `application.yaml` + `application-mongo.yaml` | MongoDB (JPA desactivado) |
| `relational` | `application-relational.yaml` | PostgreSQL + JPA |

Variables habituales:

| Variable | Uso |
|----------|-----|
| `SPRING_PROFILES_ACTIVE` | `mongo` o `relational` |
| `SPRING_DATA_MONGODB_URI` / `MONGO_URI` | Cadena de conexión MongoDB |
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | JDBC PostgreSQL (perfil `relational`) |
| `JWT_SECRET_KEY` | Secreto para firmar JWT (**obligatorio en producción**) |
| `PORT` | Puerto HTTP(S) (p. ej. Azure App Service suele inyectar **80**) |
| `SSL_ENABLED`, `SSL_KEYSTORE_PATH`, `SSL_KEYSTORE_PASSWORD` | TLS opcional |

Ejemplos:

```bash
# Local con MongoDB (por defecto en application.yaml)
export MONGO_URI="mongodb+srv://usuario:pass@cluster/ejemplo"
export JWT_SECRET_KEY="tu-secreto-largo-en-base64-o-texto-seguro"
mvn spring-boot:run
```

```bash
# Local con PostgreSQL
export SPRING_PROFILES_ACTIVE=relational
export DB_URL=jdbc:postgresql://localhost:5432/dosw_library
export DB_USER=postgres
export DB_PASSWORD=...
export JWT_SECRET_KEY="..."
mvn spring-boot:run
```

**Despliegue (p. ej. Azure App Service)**: suele usarse `SPRING_PROFILES_ACTIVE=mongo`, `SPRING_DATA_MONGODB_URI` (o `MONGO_URI`) y `JWT_SECRET_KEY`. Con perfil `mongo` la aplicación **no** intenta conectar a PostgreSQL en `localhost`.

## 🌐 URL base y Swagger

El puerto y el esquema dependen de `PORT` y `SSL_ENABLED`:

- Con **SSL desactivado** (por defecto en `application.yaml`): suele ser `http://localhost:80` (o el `PORT` que definas).
- Con **SSL activado** y keystore configurado: `https://localhost:<PORT>` (certificado autofirmado en local: el navegador mostrará advertencia).

Documentación interactiva:

- **Swagger UI**: `/swagger-ui/index.html`
- **OpenAPI JSON**: `/v3/api-docs`

> En endpoints protegidos: autenticarse en `POST /auth/login`, copiar el `token` y usar **Authorize** en Swagger con el prefijo `Bearer `.

### Endpoints principales

- `POST /auth/login` — login *(público)*  
- `GET /users`, `POST /users` — gestión de usuarios *(según rol)*  
- `GET /books`, `POST /books` — catálogo y altas *(según rol)*  
- `POST /loans`, `PUT /loans/return`, `GET /loans/my` — préstamos *(según rol)*  

La lista exacta y los cuerpos de petición están en Swagger.

## 🧪 Pruebas y calidad

```bash
mvn clean test
```

Reporte JaCoCo: `target/site/jacoco/index.html`

En tests se usa perfil **`relational`** con **H2** en memoria y Mongo desactivado (ver `src/test/resources/application.properties`).

## 📁 Estructura del código

Más diagramas y detalle en [Arquitectura](src/main/README.md).

```text
src/main/java/edu/eci/dosw/tdd/
├── config/                 # Security, JWT, Swagger, CORS, etc.
├── controller/             # REST, DTOs, mappers, manejo global de errores
├── core/                   # Dominio: modelos, servicios, validadores, excepciones
│   └── repository/         # Interfaces de repositorio (puertos)
├── persistence/
│   ├── nonrelational/      # Perfil mongo: documentos, mappers, MongoRepository
│   └── relational/         # Perfil relational: entidades JPA, mappers, JpaRepository
└── DoswLibraryApplication.java
```

## 📎 Enlaces

- [**High Level Design**](docs/HLD_DOSW_Library.pdf)
- [Videos explicativos](docs/videos.md)
- [Cobertura y pruebas](src/test/README.md)
- [Arquitectura UML / Mermaid (detalle)](src/main/README.md)

---

*Proyecto de la asignatura **Desarrollo de Operaciones de Software (DOSW)** — ECI.*
