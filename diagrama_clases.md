# Diagrama de Clases de la Biblioteca

Este diagrama ilustra las clases principales del sistema separadas convenientemente en Modelos, Controladores (API), Servicios (Lógica) y Repositorios (Persistencia), omitiendo deliberadamente la extensa capa de mapeos (`Mappers`) y excepciones (`Exceptions`) para facilitar su lectura.

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
