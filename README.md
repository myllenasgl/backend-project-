# Professor Allocation - REST API

API REST para gerenciamento de professores, departamentos, cursos e alocação de horários (professor x curso x dia/hora), com detecção de conflito de agenda. Projeto desenvolvido como trabalho de pós-graduação.

---

## Integrantes do Grupo

1. **Ana Beatriz**
2. **Kleber Fanini**
3. **Myllena Lelis**

*(Listados em ordem alfabética)*

---

## Pontos de Melhoria Implementados (Nota Máxima: 10,00)

Para atender a todos os requisitos do trabalho e buscar a nota máxima (10,00), cada um dos 3 integrantes do grupo ficou responsável pela implementação e decisão técnica de um ponto de melhoria específico:

### 1. Manipulador de Exceções (*Exception Handlers*) 
* **Decisão & Implementação**:
  * Removidos todos os blocos `try/catch` dos métodos em todas as classes de controle (`AllocationController`, `CourseController`, `DepartmentController`, `ProfessorController`), mantendo a camada de controle limpa e focada no roteamento HTTP.
  * Criada a classe `GlobalExceptionHandler` anotada com `@RestControllerAdvice`, centralizando o tratamento de todas as exceções lançadas pela aplicação.
  * Mapeamento de cada tipo de exceção para seu código HTTP e resposta padronizada (objeto `ApiError`):
    * `ResourceNotFoundException` → Retorna HTTP **404 Not Found** (quando uma entidade não é encontrada por ID).
    * `ScheduleConflictException` → Retorna HTTP **409 Conflict** (quando há colisão de horário no cadastro de alocação de professor).
    * `BusinessRuleException` → Retorna HTTP **400 Bad Request** (violações de regras de negócio, como horário final menor que o inicial).
    * `MethodArgumentNotValidException` → Retorna HTTP **400 Bad Request** (detalhes dos atributos inválidos no corpo da requisição).
    * `Exception` → Retorna HTTP **500 Internal Server Error** (tratamento genérico para erros inesperados).

### 2. Objetos de Transferência de Dados (*Data Transfer Objects* - DTOs)
* **Decisão & Implementação**:
  * As entidades JPA (`Department`, `Professor`, `Course`, `Allocation`) foram completamente desacopladas da API e não são mais expostas nos endpoints.
  * Criados objetos de transferência de dados específicos para requisição (`*RequestDTO`) e resposta (`*ResponseDTO`), mapeados via classes auxiliares `*Mapper`.
  * **Resolução de Referência Cíclica**: O relacionamento bidirecional entre `Course` e `Allocation` gerava loop infinito de serialização JSON (`StackOverflowError`). Com o uso do `CourseResponseDTO` e `AllocationResponseDTO`, a serialização foi resolvida de forma limpa, sem expor a lista recursiva de alocações e sem depender de anotações como `@JsonIgnore` nas entidades JPA.
  * **Respostas de Erro com Corpo**: Permitiu que os fluxos de erro contivessem um corpo de resposta padronizado via DTO `ApiError`.

### 3. Uso de Validadores (*Bean Validation*)  
* **Decisão & Implementação**:
  * Aplicadas anotações de Bean Validation (`@NotBlank`, `@NotNull`) nos DTOs de requisição (`AllocationRequestDTO`, `CourseRequestDTO`, `DepartmentRequestDTO`, `ProfessorRequestDTO`) para validação prévia dos atributos.
  * No campo `cpf` da classe `ProfessorRequestDTO`, foi aplicada a anotação `@CPF` do Hibernate Validator, garantindo a validação dos dígitos verificadores reais do CPF (não apenas do formato/tamanho da string).
  * Incluída a anotação `@Valid` no parâmetro `@RequestBody` dos métodos de `POST` e `PUT` nas controllers.
  * **Benefício**: Evita que requisições com dados nulos, em branco ou CPFs semanticamente inválidos cheguem às camadas de serviço e repositório, retornando imediatamente HTTP 400 com os campos divergentes.

---

## Diferenciais Avançados de Backend Implementados

Além dos 3 pontos de melhoria exigidos, foram implementados **dois módulos diferenciais avançados na camada backend**:

### 1. Módulo de Analytics & Carga Horária (`ReportService` / `ReportController`)
* **O que é**: Um serviço especializado na camada backend responsável pelo cálculo em tempo real de estatísticas e métricas de alocação de professores.
* **Como funciona**:
  * **Cálculo da Carga Horária Semanal (`GET /reports/workload`)**: Utiliza a **Java 8+ Date/Time API (`java.time.Duration`)** e **Java Streams** para calcular o tempo total em horas/minutos acumulado por professor a partir das suas alocações ativas (`startHour` até `endHour`).
  * **Dashboard Consolidado (`GET /reports/dashboard`)**: Agrega e consolida em uma única resposta JSON a contagem total de professores, departamentos, cursos, alocações ativas e a lista detalhada de carga horária por docente.

### 2. Módulo de Exportação de Agendas (`CSV` & `iCalendar .ics`)
* **O que é**: Funcionalidade no backend que gera arquivos de agenda nos formatos padronizados **CSV** e **iCalendar (RFC 5545 `.ics`)**.
* **Como funciona**:
  * **Exportação CSV (`GET /professors/{id}/schedule/export/csv`)**: Constrói um fluxo de texto formatado em CSV contendo o dia da semana, horários de início/fim, disciplina e departamento do professor, definindo os cabeçalhos HTTP `Content-Type: text/csv` e `Content-Disposition: attachment` para download direto no navegador ou cliente HTTP.
  * **Exportação iCalendar (.ics) (`GET /professors/{id}/schedule/export/ics`)**: Monta um documento `.ics` estruturado com as marcas `VCALENDAR`, `VEVENT` e regras de recorrência semanal (`RRULE:FREQ=WEEKLY`), permitindo que o professor ou aluno baixe o arquivo e **importe sua grade horária diretamente no Google Agenda, Apple Calendar ou Microsoft Outlook**.
    * *Como abrir/visualizar o arquivo .ics*:
      * **Visualmente (Calendários)**: Dê 2 cliques no arquivo baixado para abrir no Calendário do Windows / Outlook, ou importe no Google Agenda em *Configurações ➔ Importar e exportar*.
      * **Estrutura de Código (Editor de Texto)**: Abra o arquivo `.ics` com o Bloco de Notas ou VS Code para inspecionar a estrutura de dados padronizada (RFC 5545) gerada pelo backend.

---

## Outras Melhorias e Ordenações

* **Ordenação na Camada de Serviços**:
  * **Departamentos**: Ordenados alfabeticamente pelo nome (`name`).
  * **Cursos**: Ordenados alfabeticamente pelo nome (`name`).
  * **Professores**: Ordenados alfabeticamente pelo nome (`name`) nas consultas gerais e por departamento.
  * **Alocações**: Ordenadas pelo dia da semana (`dayOfWeek`) e, secundariamente, pelo horário inicial (`startHour`).

---

## Stack Tecnológica

- Java 17
- Spring Boot 4.1.0 (Spring Web, Spring Data JPA, Bean Validation)
- MySQL / H2 Database (para testes)
- Lombok
- springdoc-openapi (Swagger UI)

---

## Como Configurar e Rodar

1. Certifique-se de ter um servidor MySQL rodando localmente (or ajuste para banco em memória/H2 no `application.properties`).
2. Ajuste o usuário e senha no arquivo `src/main/resources/application.properties` (por padrão utiliza o banco `msgl_db`, criado automaticamente).
3. Execute o comando:

```bash
mvn spring-boot:run
```

4. A API estará disponível em `http://localhost:8080`.
5. A documentação interativa (Swagger UI) pode ser acessada em `http://localhost:8080/swagger-ui.html`.

---

## Endpoints Disponíveis

| Recurso | Endpoints |
|---|---|
| **Departamentos** | `GET /departments`, `POST /departments`, `GET /departments/{id}`, `PUT /departments/{id}`, `DELETE /departments/{id}` |
| **Professores** | `GET /professors`, `POST /professors`, `GET /professors/{id}`, `PUT /professors/{id}`, `DELETE /professors/{id}`, `GET /professors?name=`, `GET /professors/department/{department_id}` |
| **Exportação de Agenda** | `GET /professors/{id}/schedule/export/csv`, `GET /professors/{id}/schedule/export/ics` |
| **Cursos** | `GET /courses`, `POST /courses`, `GET /courses/{id}`, `PUT /courses/{id}`, `DELETE /courses/{id}` |
| **Alocações** | `GET /allocations`, `POST /allocations`, `GET /allocations/{id}`, `PUT /allocations/{id}`, `DELETE /allocations/{id}`, `GET /allocations/professor/{professor_id}`, `GET /allocations/course/{course_id}` |
| **Relatórios & Analytics** | `GET /reports/workload`, `GET /reports/dashboard` |

---

## Comentários para Avaliação do Professor

* **Conformidade com os Requisitos**: O projeto atende integralmente à especificação da API REST de alocação de professores e aos 3 pontos de melhoria solicitados, distribuídos entre os 3 integrantes do grupo.
* **Diferenciais Avançados de Backend**: Foram incluídos serviços de agregação de métricas com `java.time.Duration` e geradores de arquivos de agenda (`.csv` e `.ics` iCalendar) prontos para sincronização com o Google Agenda.
* **Respostas Tratadas e Semânticas**: Todos os erros de validação, conflitos de horário e recursos não encontrados retornam códigos de status HTTP adequados com mensagens claras e timestamps.
* **Repositório Público**: O repositório está configurado como público para acesso e avaliação.
