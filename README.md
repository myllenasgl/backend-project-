# Professor Allocation

API REST para gerenciamento de professores, departamentos, cursos e alocação de horários (professor x curso x dia/hora), com detecção de conflito de agenda.

## Stack

- Java 17
- Spring Boot 4.1.0 (Spring Web, Spring Data JPA, Bean Validation)
- MySQL
- Lombok
- springdoc-openapi (Swagger UI)

## Estrutura de pacotes

```
com.project.professor.allocation
├── config       # CORS, OpenAPI/Swagger
├── controller   # Endpoints REST
├── dto          # Request/Response DTOs (entrada e saída da API)
├── entity       # Entidades JPA
├── exception    # Exceções de negócio + handler global (@RestControllerAdvice)
├── mapper       # Conversão entre entidade e DTO
├── repository   # Interfaces Spring Data JPA
└── service      # Regras de negócio
```

## Como configurar e rodar

1. Tenha um MySQL rodando localmente.
2. Ajuste usuário/senha em `src/main/resources/application.properties` se necessário (por padrão usa `root` sem senha, banco `msgl_db`, criado automaticamente).
3. Rode:

```bash
mvn spring-boot:run
```

4. A API sobe em `http://localhost:8080`. Documentação interativa (Swagger UI) em `http://localhost:8080/swagger-ui.html`.

## Endpoints

| Recurso | Endpoints |
|---|---|
| Departamentos | `GET/POST /departments`, `GET/PUT/DELETE /departments/{id}` |
| Professores | `GET/POST /professors`, `GET/PUT/DELETE /professors/{id}`, `GET /professors?name=`, `GET /professors/department/{department_id}` |
| Cursos | `GET/POST /courses`, `GET/PUT/DELETE /courses/{id}` |
| Alocações | `GET/POST /allocations`, `GET/PUT/DELETE /allocations/{id}`, `GET /allocations/professor/{professor_id}`, `GET /allocations/course/{course_id}` |

Detalhes completos de cada endpoint (parâmetros, corpo de requisição, respostas) estão no Swagger UI.

## Melhorias implementadas para a entrega final

Myllena Lelis fez:

1. **Exception Handlers** — criado um `GlobalExceptionHandler` (`@RestControllerAdvice`) que trata cada tipo de exceção separadamente (`ResourceNotFoundException` → 404, `ScheduleConflictException` → 409, `BusinessRuleException` → 400, erros de validação → 400, erro genérico → 500), todos com corpo de resposta padronizado. Os `try/catch` que existiam em todos os controllers foram removidos.
2. **DTOs** — as entidades JPA deixaram de ser expostas diretamente na API. Cada recurso tem um `RequestDTO` (entrada) e um `ResponseDTO` (saída), convertidos por classes `*Mapper`. Isso também resolveu um problema real de referência cíclica na serialização entre `Course` e `Allocation` (que antes podia causar erro ao listar cursos).
3. **Validadores** — os `RequestDTO`s usam Bean Validation (`@NotBlank`, `@NotNull`) para garantir que campos obrigatórios não cheguem nulos/vazios nas camadas de serviço.
4. **Validação de CPF** — o campo `cpf` do professor usa a anotação `@CPF` (Hibernate Validator), que valida os dígitos verificadores reais do CPF, não só o formato.

Ana Beatriz Fez:

Ordenação na camada de serviços - Departamentos: ordenados alfabeticamente pelo nome.
Cursos: ordenados alfabeticamente pelo nome.
Professores: ordenados alfabeticamente pelo nome nas consultas gerais, por nome e por departamento.
Alocações: ordenadas pelo dia da semana e, dentro do mesmo dia, pelo horário inicial.
