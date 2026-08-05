
```markdown
# 🚀 Guia de Configuração do Projeto Mediway

Este documento descreve passo a passo como configurar o ambiente para rodar a API **Java/Spring Boot** utilizando **Docker Compose** com **MySQL** e **RabbitMQ**.

---

## 🛠️ 1. Instalação do Docker e Docker Desktop

1. **Baixar e instalar o Docker Desktop**
   - Acesse [Docker Desktop](https://www.docker.com/products/docker-desktop) e baixe a versão para Windows.
   - Instale normalmente.

2. **Configurar WSL 2 (Windows Subsystem for Linux)**
   - Abra o **PowerShell** como administrador e rode:
     ```powershell
     wsl --install
     ```
   - Para verificar se está ativo:
     ```powershell
     wsl --list --verbose
     ```
     → O resultado deve mostrar `VERSION = 2`.  
   - Se estiver como versão 1, rode:
     ```powershell
     wsl --set-version Ubuntu 2
     ```

3. **Verificar instalação do Docker**
   - Abra o terminal e rode:
     ```bash
     docker --version
     docker-compose --version
     ```

---

## 📂 2. Estrutura do Projeto

Na raiz do projeto existe uma pasta chamada **docker** contendo o arquivo `docker-compose.yml`:

```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: mysql_mediway
    restart: unless-stopped
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: admin
      MYSQL_DATABASE: db_Mediway
      MYSQL_USER: admin
      MYSQL_PASSWORD: admin
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"

volumes:
  mysql_data:
```

---

## ▶️ 3. Subindo os Containers

1. No terminal, vá até a pasta `docker`:
   ```bash
   cd docker
   ```

2. Rode:
   ```bash
   docker-compose up -d
   ```

3. Verifique se estão rodando:
   ```bash
   docker ps
   ```

4. Para parar:
   ```bash
   docker-compose down
   ```
OBS: Mantenha o docker dekstop aberto enquanto estiver rodando os containers

## ⚠️ 3.1. Possível conflito de portas com MySQL instalado localmente
Se você já tiver o MySQL instalado na máquina (por exemplo, usando o MySQL Workbench), 
pode ocorrer conflito porque o container também tenta usar a porta 3306.
Sintoma: o container do MySQL não sobe ou dá erro de bind na porta.
Solução: alterar a porta mapeada no docker-compose.yml. Exemplo:

```yml
ports:
  - "3307:3306"  
```
Nesse caso, o MySQL dentro do container continua rodando na porta 3306, mas externamente será acessado pela porta 3307.
Se alterar a porta, também é necessário atualizar o application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3307/db_Mediway
```
---

## 🗝️ 4. Gerando as Chaves JWT

As chaves são usadas para assinar e validar tokens JWT.

1. Abra o **Git Bash** na raiz do projeto.
2. Rode:
   ```bash
   openssl genrsa -out private.pem 2048
   openssl rsa -in private.pem -pubout -out public.pem
   ```
3. Coloque os arquivos `private.pem` e `public.pem` dentro da pasta `resources` do projeto.

---

## ⚙️ 5. Configuração da API (Spring Boot)

O arquivo `application.properties` já está configurado para:

- **Banco de dados MySQL** → `jdbc:mysql://localhost:3306/db_Mediway`
- **RabbitMQ** → `localhost:5672`
- **JWT** → chaves `private.pem` e `public.pem`

Antes de rodar a API, certifique-se que:
- Containers MySQL e RabbitMQ estão rodando (`docker ps`).
- As chaves foram geradas e estão no caminho correto.

---

## 🚀 6. Rodando a API

1. Na raiz do projeto, compile e rode:
   ```bash
   ./mvnw spring-boot:run
   ```
   ou, se usar Gradle:
   ```bash
   ./gradlew bootRun
   ```

2. A API vai se conectar ao MySQL e RabbitMQ automaticamente.

---

## 📌 Resumo

1. Instalar Docker Desktop e ativar WSL 2.
2. Clonar o projeto e entrar na pasta `docker`.
3. Rodar `docker-compose up -d`.
4. Gerar as chaves JWT com `openssl`.
5. Confirmar que containers estão rodando.
6. Rodar a API com Maven/Gradle.

---
```

---

# Revisao Arquitetural - API Mediway

> **Revisao realizada em:** 2026-08-05
> **Arquiteto:** Revisao automatizada com Claude (Senior Software Architect - Java/Spring Boot)
> **Escopo:** Varredura completa de todos os arquivos `.java`, configuracoes e dependencias do projeto.

---

## Legenda de Severidade

| Severidade | Descricao |
|---|---|
| **CRITICO** | Falha de seguranca, perda de dados ou indisponibilidade em producao. Corrigir antes de qualquer deploy. |
| **IMPORTANTE** | Degrada qualidade, confiabilidade ou mantenabilidade. Corrigir na proxima sprint. |
| **SUGESTAO** | Boas praticas, consistencia e melhorias de longo prazo. |

---

## CRITICO

### C1 — Credenciais reais expostas no repositorio Git

- **Arquivo:** `src/main/resources/application-local.properties`
- **Linhas:** 7-9
- **Problema:** O arquivo contem o e-mail `bruno7motadev@gmail.com` e uma senha de aplicativo real do Gmail (`qgci wkdf yrsg xbsu`) commitados no historico do Git. Qualquer pessoa com acesso ao repositorio pode usar essas credenciais para enviar e-mails arbitrarios em nome da conta.
- **Acao imediata:**
  1. Revogar/regenerar a App Password do Gmail em `myaccount.google.com/security`.
  2. Adicionar `application-local.properties` ao `.gitignore` e remover do historico com `git filter-repo` ou BFG Repo Cleaner.
  3. Usar variaveis de ambiente ou um cofre de segredos (ex: `.env` + `@Value`) para todas as credenciais.

---

### C2 — Endpoint publico expondo dados medicos de pacientes

- **Arquivo:** `src/main/java/br/com/api_mediway/consultation/ConsultationController.java`
- **Linhas:** 87-95
- **Problema:** O endpoint `GET /consultation/by-status` nao possui a anotacao `@PreAuthorize`. Por consequencia, qualquer requisicao sem autenticacao consegue listar consultas medicas de pacientes filtrando por status, expondo dados sensiveis de saude (PHI). Todos os outros endpoints do controller estao protegidos; este ficou descoberto.
- **Acao imediata:** Adicionar `@PreAuthorize` com as roles apropriadas (ex: `SCOPE_ADMIN`, `SCOPE_MEDICO`, `SCOPE_CUIDADOR`) ao metodo `getConsultationByStatus`.

---

### C3 — Ausencia total de validacao de entrada nos controllers (`@Valid`)

- **Arquivos:** Todos os controllers (`AuthController`, `PatientController`, `ConsultationController`, `MedicationController`, etc.)
- **Linhas:** Todos os parametros `@RequestBody`
- **Problema:** Nenhum parametro `@RequestBody` possui a anotacao `@Valid`. Campos como `password`, `email`, `crm`, `dateOfBirth` e `nome` chegam aos services sem qualquer validacao de formato, tamanho ou nulidade. Isso permite a entrada de dados malformados, cadeia de erros silenciosos (NPE), e pode ser vetor de injecao de dados invalidos no banco.
- **Acao:** Adicionar `@Valid` em todos os `@RequestBody` dos controllers e usar anotacoes Bean Validation (`@NotBlank`, `@Email`, `@NotNull`, `@Size`) nas classes DTO de request correspondentes.

---

### C4 — `ddl-auto=update` ativo sem ferramenta de migracao de schema

- **Arquivo:** `src/main/resources/application.properties`
- **Linha:** (propriedade `spring.jpa.hibernate.ddl-auto=update`)
- **Problema:** O modo `update` faz o Hibernate tentar aplicar diferencas de schema automaticamente em cada inicializacao. Em producao, isso e perigoso: pode corromper o banco em migrações complexas (renomeacao de colunas, mudanca de tipo), nao e reversivel, e nao tem historico de versao. Nao ha Flyway nem Liquibase no projeto.
- **Acao:** Migrar para `ddl-auto=validate` em producao e adotar Flyway ou Liquibase para versionamento controlado de schema.

---

### C5 — Ausencia de rate limiting no fluxo de reset de senha (brute-force)

- **Arquivo:** `src/main/java/br/com/api_mediway/auth/AuthService.java`
- **Linhas:** 117-139
- **Problema:** O codigo de reset de senha e um numero de 6 digitos (`000000` a `999999`), gerando apenas 1.000.000 de combinacoes possiveis. Nao ha limite de tentativas por IP ou por usuario, nem lockout apos falhas consecutivas. Um atacante pode automatizar requisicoes para `POST /auth/validate-code` e quebrar o codigo em minutos, assumindo qualquer conta da plataforma.
- **Acao:** Implementar rate limiting no endpoint (ex: Bucket4j, Spring Rate Limiter), bloquear o codigo apos N tentativas invalidas, e considerar aumentar a entropia do codigo (alfanumerico, UUID parcial, ou TOTP).

---

## IMPORTANTE

### I1 — `@Data` (Lombok) em entidades JPA — anti-padrao conhecido

- **Arquivos:** `User.java`, `PatientInfos.java`, `DoctorInfos.java`, `CaregiverInfos.java`, `Consultation.java`, `Exam.java`, `Medication.java`, `Vaccine.java`, `MedicineBox.java`
- **Linhas:** Declaracao da classe em cada entidade
- **Problema:** `@Data` gera `equals()` e `hashCode()` baseados em **todos** os campos, incluindo colecoes com `FetchType.LAZY`. Isso causa `LazyInitializationException` ao comparar entidades fora de um contexto de persistencia ativo, e pode gerar loops infinitos em relacionamentos bidirecionais (ex: `PatientInfos <-> MedicineBox <-> Medication`). Alem disso, `@ToString` gerado por `@Data` inclui colecoes lazy, acionando carregamento nao intencional.
- **Acao:** Substituir `@Data` por `@Getter @Setter` nas entidades. Implementar `equals()`/`hashCode()` baseados apenas na chave primaria (usando `@EqualsAndHashCode(of = "id")`). Usar `@ToString(exclude = {...})` excluindo as colecoes.

---

### I2 — Dependencia Firebase Admin SDK sem uso no codigo

- **Arquivo:** `pom.xml`
- **Dependencia:** `com.google.firebase:firebase-admin:9.2.0`
- **Problema:** A dependencia esta declarada mas nenhuma classe ou configuracao Firebase foi encontrada em todo o codigo-fonte. Dependencias nao utilizadas aumentam o tamanho do artefato, ampliam a superficie de ataque (vulnerabilidades no SDK transitam para o projeto) e confundem a equipe de manutencao.
- **Acao:** Remover a dependencia do `pom.xml`. Se houver planos de uso futuro, adicionar somente quando a implementacao comecar.

---

### I3 — `@EnableScheduling` declarado na classe `SecurityConfig`

- **Arquivo:** `src/main/java/br/com/api_mediway/common/config/SecurityConfig.java`
- **Linha:** Declaracao da classe (~linha 14)
- **Problema:** `@EnableScheduling` e uma responsabilidade de configuracao de agendamento, nao de seguranca. Tê-la em `SecurityConfig` viola o Single Responsibility Principle e dificulta a leitura e manutencao da configuracao de seguranca, que ja e intrinsecamente complexa.
- **Acao:** Criar uma classe dedicada `SchedulingConfig.java` em `common/config/` com `@Configuration` e `@EnableScheduling`.

---

### I4 — Sem paginacao nas listagens — risco de OutOfMemoryError

- **Arquivos:** `PatientController.java` (`getAllPatients`), `DoctorController.java` (`getAll`), `AdminController.java` (`getAllUsers`), `DoctorInfosRepository.java` (`findBySpecialtyIgnoreCase`, `findByAvailability`)
- **Problema:** Endpoints de listagem retornam `List<T>` sem qualquer paginacao. Com o crescimento do banco de dados (que em uma plataforma medica pode ter milhares de pacientes e consultas), uma unica requisicao pode carregar toda a tabela em memoria, causando `OutOfMemoryError` ou degradacao severa de performance.
- **Acao:** Adicionar `Pageable` como parametro nos metodos de repositorio e service, e retornar `Page<T>` nos controllers. Usar `@PageableDefault` para definir tamanhos padrao seguros.

---

### I5 — Cobertura de testes praticamente inexistente

- **Arquivo:** `src/test/java/br/com/api_mediway/ApiMediwayApplicationTests.java`
- **Problema:** O unico teste existente e `contextLoads()`, que verifica apenas se o contexto Spring inicializa. Nao ha testes unitarios para logica de negocio (validacao de roles, reset de senha, agendamento de consultas), testes de integracao para os endpoints REST, nem testes de seguranca para as regras de autorizacao (`@PreAuthorize`). A falha no C2 (endpoint publico) so foi identificada por revisao manual — um teste teria capturado isso automaticamente.
- **Acao:** Implementar testes com `@WebMvcTest` para controllers (verificando autorizacao), `@DataJpaTest` para repositorios, e testes unitarios para services criticos (`AuthService`, `PasswordResetCodeService`).

---

### I6 — Campo `age` em `PatientInfos` ficara desatualizado automaticamente

- **Arquivo:** `src/main/java/br/com/api_mediway/patient/entity/PatientInfos.java`
- **Campo:** `Integer age`
- **Problema:** A idade e armazenada como valor estatico no banco. Um paciente cadastrado com 45 anos continuara com 45 anos registrado para sempre, a menos que seja manualmente atualizado. Em um sistema medico, a idade incorreta pode ter consequencias clinicas.
- **Acao:** Remover o campo `age` da entidade e calcular dinamicamente a partir de `dateOfBirth` com `Period.between(dateOfBirth, LocalDate.now()).getYears()` no mapper de response. O campo `age` do DTO de request deve ser ignorado ou removido.

---

### I7 — `RoleRepository.findByName()` retorna tipo sem Optional

- **Arquivo:** `src/main/java/br/com/api_mediway/user/repository/RoleRepository.java`
- **Linha:** Assinatura do metodo `findByName(String name)`
- **Problema:** O metodo retorna `Role` diretamente (nao `Optional<Role>`). Embora `AuthService.toRegister()` ja faca a verificacao de nulo na linha 73, o contrato do repositorio permite retorno `null`, o que e um anti-padrao em Java moderno e perigoso para qualquer outro consumidor futuro do metodo que nao faca a verificacao.
- **Acao:** Alterar a assinatura para `Optional<Role> findByName(String name)` e ajustar os consumidores para usar `.orElseThrow()`.

---

### I8 — Relacionamento bidirecional sem protecao contra loop de serializacao

- **Arquivos:** `PatientInfos.java` ↔ `MedicineBox.java` ↔ `Medication.java`
- **Problema:** As entidades possuem referencias circulares (`PatientInfos` tem `MedicineBox`, que tem `PatientInfos`; `MedicineBox` tem `List<Medication>`, que tem `MedicineBox`). Sem configuracao Jackson (`@JsonManagedReference`/`@JsonBackReference` ou `@JsonIgnore`), a serializacao direta da entidade causaria `StackOverflowError`. O problema esta sendo contornado pelos mappers, mas o risco permanece se alguma entidade for serializada diretamente por engano.
- **Acao:** Adicionar `@JsonIgnore` nos lados "filho" dos relacionamentos nas entidades, como camada de seguranca defensiva, mesmo que os mappers ja tratem a conversao.

---

## SUGESTAO / MELHORIA

### S1 — Typos em enums e campos que podem impactar a API

- **Arquivos e locais:**
  - `ConditionStatusPatient.java`: `NESSECITA_DE_ATENCAO` → deveria ser `NECESSITA_DE_ATENCAO`
  - `ConsultationAndExmStatus.java`: label `"Indefirido"` → deveria ser `"Indeferido"`
  - `Consultation.java`, `Exam.java`, `ConsultationRequestDTO.java`: campo `requeriments` → deveria ser `requirements`
- **Problema:** Typos em nomes de enums e campos da API sao serializados para JSON e para o banco. Corrigi-los depois de em producao exige migracao de dados e quebra de compatibilidade da API com clientes existentes. Corrigir agora tem custo zero.

---
ME


### S2 — Mistura de idiomas nos campos de entidades e DTOs

- **Exemplos:** `Medication.java` usa `nome`, `tipo`, `gaveta`, `lote` (portugues) enquanto outras entidades usam `name`, `type`, `status` (ingles). `DoctorInfos.java` usa `crm`, `specialty` (misto).
- **Problema:** Inconsistencia dificulta a leitura do codigo, a integracao com frontends e a documentacao da API. Em equipes maiores, gera duvidas sobre qual padrao seguir.
- **Acao:** Definir um padrao de idioma para o codigo (ingles e mais comum em projetos Java) e padronizar gradualmente. Documentar a decisao no `CLAUDE.md` ou `CONTRIBUTING.md`.

---

### S3 — CORS hardcoded no `SecurityConfig`

- **Arquivo:** `src/main/java/br/com/api_mediway/common/config/SecurityConfig.java`
- **Problema:** A origem `http://localhost:3000` esta fixada no codigo. Em staging ou producao, sera necessario alterar o codigo-fonte para cada ambiente, em vez de apenas mudar uma variavel de ambiente.
- **Acao:** Externalizar para `application.properties` com `cors.allowed-origins=http://localhost:3000` e injetar via `@Value`.

---

### S4 — Ausencia de versionamento de API

- **Arquivos:** Todos os controllers (prefixo `/auth`, `/patients`, `/doctor`, etc.)
- **Problema:** Sem prefixo de versao (ex: `/api/v1/`), qualquer mudanca quebra os clientes existentes sem possibilidade de coexistencia de versoes.
- **Acao:** Adicionar prefixo `/api/v1` nos `@RequestMapping` de todos os controllers ou via configuracao global no `application.properties` com `server.servlet.context-path=/api/v1`.

---

### S5 — Sem documentacao OpenAPI (`@Operation`, `@ApiResponse`) nos endpoints

- **Arquivos:** Todos os controllers
- **Problema:** O `springdoc-openapi` esta no `pom.xml` e o Swagger UI esta configurado, mas nenhum endpoint possui anotacoes `@Operation`, `@ApiResponse` ou `@Tag`. O Swagger gerado e basico e pouco util para consumidores da API.
- **Acao:** Adicionar anotacoes SpringDoc progressivamente, priorizando os endpoints de autenticacao e os fluxos criticos (agendamento de consultas, medicacoes).

---

### S6 — `ExamController` e `ConsultationController` nao permitem medico agendar diretamente

- **Arquivo:** `ConsultationController.java` (linha 33), `ExamController.java` (linha 34)
- **Problema:** O agendamento de consultas e exames e permitido apenas para `SCOPE_ADMIN` e `SCOPE_CUIDADOR`. O medico (`SCOPE_MEDICO`) nao pode agendar consultas, o que parece uma restricao de negocio inconsistente para uma plataforma medica.
- **Acao:** Revisar as regras de negocio com os stakeholders e ajustar o `@PreAuthorize` se necessario.

---

## Plano de Acao Prioritario

| Prioridade | Item | Estimativa | Responsavel Sugerido |
|---|---|---|---|
| **P0 - Imediato** | C1: Revogar senha Gmail e remover credenciais do Git | 30 min | Dev + DevOps |
| **P0 - Imediato** | C2: Adicionar `@PreAuthorize` em `GET /consultation/by-status` | 5 min | Dev |
| **P1 - Sprint Atual** | C3: Adicionar `@Valid` e Bean Validation nos DTOs de request | 1-2 dias | Dev |
| **P1 - Sprint Atual** | C4: Migrar para Flyway/Liquibase e `ddl-auto=validate` | 1 dia | Dev |
| **P1 - Sprint Atual** | C5: Implementar rate limiting no reset de senha | 4h | Dev |
| **P2 - Proxima Sprint** | I1: Substituir `@Data` por `@Getter @Setter` nas entidades | 2h | Dev |
| **P2 - Proxima Sprint** | I4: Paginacao nos endpoints de listagem | 1 dia | Dev |
| **P2 - Proxima Sprint** | I5: Criar suite de testes (auth, security, services) | 3-5 dias | Dev |
| **P2 - Proxima Sprint** | I6: Remover campo `age` e calcular via `dateOfBirth` | 2h | Dev |
| **P2 - Proxima Sprint** | I7: `RoleRepository.findByName()` → retornar `Optional<Role>` | 1h | Dev |
| **P3 - Backlog** | I2: Remover dependencia Firebase inutilizada | 10 min | Dev |
| **P3 - Backlog** | I3: Mover `@EnableScheduling` para `SchedulingConfig` | 15 min | Dev |
| **P3 - Backlog** | S1: Corrigir typos em enums e campos | 1h | Dev |
| **P3 - Backlog** | S3: Externalizar CORS para `application.properties` | 30 min | Dev |
| **P3 - Backlog** | S4: Adicionar prefixo `/api/v1` nos endpoints | 1h | Dev |
| **P3 - Backlog** | S5: Documentar endpoints com anotacoes SpringDoc | 2-3 dias | Dev |

---

> **Nota:** Os itens P0 bloqueiam qualquer deploy em ambiente compartilhado ou producao. Os itens P1 devem ser resolvidos antes da primeira entrega para usuarios reais. P2 e P3 podem ser tratados como divida tecnica planejada.