# Arquitectura y Diseño del Sistema 🏛️

Este documento amplía el **diseño de alto nivel** del backend con diagramas UML, Mermaid y modelos de datos. La visión consolidada (contexto, capas, perfiles `mongo`/`relational`, seguridad y despliegue) está en [**docs/high-level-design-backend.md**](../../docs/high-level-design-backend.md) *(Reto #8)*.

A continuación: detalle de la arquitectura del sistema de biblioteca a través de diagramas.

## 🏗️ Diagramas 

### 1. Diagrama de Componentes de la Biblioteca
> ![Componentes](../../docs/uml/componentes.png)

- La aplicación de biblioteca sigue una arquitectura en capas donde el 
usuario final interactúa con el sistema a través del Frontend, 
que es la interfaz de usuario. 
- El Frontend consume los servicios expuestos por el Backend 
mediante una API, solicitando información o enviando acciones 
como buscar libros, iniciar sesión o solicitar préstamos. 
- El Backend actúa como la capa de lógica de negocio, 
encargándose de procesar estas solicitudes, aplicar reglas del 
sistema y gestionar las operaciones necesarias. Finalmente, 
el Backend se comunica directamente con la Base de Datos, 
que es la capa de persistencia donde se almacenan y administran 
los datos de libros, usuarios y préstamos, garantizando que el 
Frontend nunca acceda a los datos de forma directa por razones de 
seguridad y 
organización.

---

### 2. Diagrama Específico de Componentes

```mermaid
flowchart LR
    %% Definición de Controladores (API)
    subgraph Web [Capa Web / API]
        direction TB
        BC[BookController]
        UC[UserController]
        LC[LoanController]
        AC[AuthController]
    end

    %% Definición de Servicios (Lógica de Negocio)
    subgraph Core [Capa de Lógica de Negocio]
        direction TB
        BS[BookService]
        US[UserService]
        LS[LoanService]
        JS[JwtService]
    end

    %% Apoyo y Reglas
    subgraph Utils [Componentes de Apoyo]
        V[Validator]
    end

    %% Relaciones Controlador -> Servicio
    BC == Delega ==> BS
    UC == Delega ==> US
    LC == Delega ==> LS
    AC == Delega ==> JS

    %% Relaciones internas entre servicios (El LoanService necesita de los otros dos)
    LS -. "Verifica stock" .-> BS
    LS -. "Verifica estado" .-> US

    %% Dependencia de Validadores
    LS -. "Aplica Reglas" .-> V
```

Este diagrama ilustra una arquitectura de software multicapa diseñada bajo
el principio de Separación de Responsabilidades (SoC), donde la Capa Web 
actúa como el punto de entrada mediante controladores que gestionan las 
peticiones externas. Estos controladores se comunican con la Capa de 
Lógica de Negocio a través de interfaces de servicio (representadas 
por los conectores circulares), lo que garantiza un desacoplamiento 
que facilita el mantenimiento y la escalabilidad del sistema.
En el núcleo, los servicios procesan las reglas de negocio,
destacando el componente LoanService como un eje central que 
interactúa con BookService y UserService para verificar la
disponibilidad y el estado de los usuarios antes de ejecutar 
un préstamo. Finalmente, el flujo se apoya en un Validator 
transversal para asegurar que todas las operaciones cumplan 
con las reglas de integridad del sistema antes de ser procesadas.

---

### 3. Diagrama de Clases

```mermaid
classDiagram
    %% Modelos / Entidades de Dominio
    class User {
        - Long id
        - String name
        - String username
        - String password
        - String role
    }

    class Book {
        - Long id
        - String title
        - String author
        - Integer totalQuantity
        - Integer availableQuantity
    }

    class Loan {
        - Long id
        - Long userId
        - Long bookId
        - LocalDate loanDate
        - LocalDate returnDate
        - LoanStatus status
    }

    class LoanStatus {
        <<enumeration>>
        ACTIVE
        RETURNED
    }
    
    Loan --> "1" LoanStatus : tiene

    %% Controladores REST
    class UserController {
        + registerUser(UserDTO) UserDTO
        + getAllUsers() List~UserDTO~
    }

    class BookController {
        + addBook(BookDTO) BookDTO
        + getAllBooks() List~BookDTO~
    }

    class LoanController {
        + createLoan(Long, Long) LoanDTO
        + returnLoan(Long, Long) LoanDTO
        + getMyLoans() List~LoanDTO~
    }

    class AuthController {
        + login(LoginRequest) LoginResponse
    }

    %% Lógica de Negocio
    class UserService {
        + registerUser(User) User
        + findByUsername(String) User
    }

    class BookService {
        + addBook(Book) Book
        + getBookById(Long) Book
    }

    class LoanService {
        + createLoan(Long, Long) Loan
        + returnLoan(Long, Long) Loan
        + getLoansByUserId(Long) List~Loan~
    }
    
    class JwtService {
        + generateToken(UserDetails) String
        + extractUsername(String) String
        + isTokenValid(String, UserDetails) boolean
    }

    %% Capa de Datos (JPA)
    class UserRepository {
        <<interface>>
        + findByUsername(String) Optional~UserEntity~
    }
    
    class BookRepository {
        <<interface>>
    }

    class LoanRepository {
        <<interface>>
        + findByUserId(Long) List~LoanEntity~
        + findByBookIdAndStatus(Long, String) List~LoanEntity~
    }

    %% Relaciones estructurales (Dependencias)
    UserController ..> UserService : delega
    BookController ..> BookService : delega
    LoanController ..> LoanService : delega
    AuthController ..> JwtService : delega
    
    UserService ..> UserRepository : persiste
    BookService ..> BookRepository : persiste
    LoanService ..> LoanRepository : persiste
    
    LoanService ..> UserService : requiere
    LoanService ..> BookService : requiere
    
    UserService ..> User : gestiona
    BookService ..> Book : gestiona
    LoanService ..> Loan : gestiona
```

Este sistema sigue una arquitectura de tres capas que separa las 
responsabilidades de forma clara: los Controladores gestionan las 
peticiones externas y la seguridad mediante JwtService, los Servicios
ejecutan la lógica de negocio y validaciones entre componentes, y los 
Repositorios se encargan de la persistencia de datos. 
El modelo de dominio, compuesto por User, Book y Loan, 
define las reglas e integridad de la información, 
permitiendo que el LoanService actúe como orquestador 
principal al requerir datos de usuarios y libros para 
procesar préstamos de manera segura y eficiente.

--- 

### 4. Módelo entidad - relación 

> ![Diagrama Módelo entidad](../../docs/parte2/3NF.png)

El diagrama muestra una arquitectura de base de datos clara y 
bien normalizada para gestionar una biblioteca. La relación muchos 
a muchos entre USERS y BOOKS, usando la tabla LOANS, permite registrar 
cada préstamo y mantener la integridad de los datos. 
Además, el control entre total_quantity y available_quantity ayuda a manejar el 
inventario en tiempo real sin inconsistencias. 
En general, es un modelo simple pero sólido para controlar libros y usuarios.


--- 

### 4. Diagrama Modelo No Relacional 


```mermaid	


classDiagram
    direction TB

    %% ==========================================
    %% CAPA LÓGICA (Clases de tu aplicación)
    %% ==========================================
    class User {
        
    }
    
    class Book {
        
    }
    
    class Metadata {
        
    }
    
    class Availability {
        
    }
    
    class Loan {
        
    }
    
    class History {
        
    }

    %% Relaciones de Embebido (Composición: Diamante Negro)
    %% Significan que viven dentro del documento
    Book *-- Metadata : /metadata
    Book *-- Availability : /availability
    Loan *-- History : /history

    %% Relaciones de Referencia (Agregación: Diamante Blanco)
    %% Significan que solo guardamos el ID
    Loan o-- User : /user_id
    Loan o-- Book : /book_id

    %% ==========================================
    %% CAPA FÍSICA (NoSQL Documents)
    %% ==========================================
    namespace NoSQL_Database {
        class User_Document {
            <<Document>>
        }
        class Book_Document {
            <<Document>>
        }
        class Loan_Document {
            <<Document>>
        }
    }

    %% Flechas de Mapeo (Líneas punteadas hacia los documentos)
    %% Mapeo de Usuario
    User ..|> User_Document

    %% Mapeo de Libro y sus embebidos hacia el MISMO documento
    Book ..|> Book_Document
    Metadata ..|> Book_Document
    Availability ..|> Book_Document

    %% Mapeo de Préstamo y su historial hacia el MISMO documento
    Loan ..|> Loan_Document
    History ..|> Loan_Document

```

Este diseño refleja una arquitectura híbrida de persistencia NoSQL muy bien pensada para una base de datos documental como MongoDB. Lo más destacado es cómo aprovechas el patrón de embebido (composición) para agrupar datos que siempre se consultan juntos, como los detalles de Metadata y Availability dentro del documento Book, lo que reduce drásticamente el número de lecturas necesarias en el servidor. Al mismo tiempo, utilizas con acierto el patrón de referencia (agregación) en Loan para vincular usuarios y libros mediante IDs, evitando la duplicación masiva de datos que suelen cambiar con frecuencia. En resumen, el diagrama muestra una transición clara de la lógica de negocio a una estructura física optimizada para minimizar los JOINs en tiempo de ejecución, manteniendo la integridad referencial solo donde es estrictamente necesario.