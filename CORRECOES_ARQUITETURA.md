# Registro de Correções — Revisão Arquitetural API Mediway

> Este arquivo complementa a seção "Revisao Arquitetural" do `README.md`.
> Enquanto o README descreve **o problema encontrado**, este arquivo registra
> **o que foi de fato alterado, quando e por quê**, item por item (C1, C2, C3...).
>
> Ao resolver um novo item da revisão, adicione uma nova seção seguindo o
> modelo abaixo em vez de reescrever o histórico existente.

---

## Índice de status

| Item | Descrição curta | Status | Data |
|---|---|---|---|
| C1 | Credenciais reais expostas no repositório | 🟡 Parcial (ação manual pendente) | 2026-08-05 |
| C2 | Endpoint público expondo dados médicos (`/consultation/by-status`) | ✅ Resolvido | 2026-08-05 |
| C3 | Ausência de `@Valid` nos controllers | ✅ Resolvido | 2026-08-06 |
| C4 | `ddl-auto=update` sem Flyway/Liquibase | ✅ Resolvido | 2026-08-06 |
| C5 | Sem rate limiting no reset de senha | ⬜ Não iniciado | — |
| I1–I8 | Ver README.md | ⬜ Não iniciado | — |
| S1–S6 | Ver README.md | ⬜ Não iniciado | — |

---

## C1 — Credenciais reais expostas no repositório Git

- **Status:** 🟡 Parcial — correção de código feita; rotação de credenciais e limpeza do histórico do Git **exigem ação manual do responsável pelo repositório**.
- **Data:** 2026-08-05

### O que foi alterado

- `src/main/resources/application-local.properties`: as credenciais reais do
  Gmail (e-mail e senha de app), que estavam em texto plano, foram substituídas
  por placeholders de variável de ambiente (`${MAIL_USERNAME}`, `${MAIL_PASSWORD}`),
  no mesmo padrão já usado em `application.properties`. As demais propriedades
  (RabbitMQ) passaram a ter valores padrão via `${VAR:default}` em vez de
  literais fixos.
- Adicionado comentário no topo do arquivo explicando que ele é ignorado pelo
  Git e não deve conter segredos em texto plano.
- Confirmado (já estava presente como alteração pendente no `.gitignore`) que
  `src/main/resources/application-*.properties` está ignorado, mantendo apenas
  `application.properties` (que usa apenas placeholders `${VAR}`) versionado.

### Por quê

O arquivo continha um e-mail e uma senha de app do Gmail reais, hard-coded.
Hard-coding de segredo é a causa raiz do vazamento: mesmo com `.gitignore`
correto, qualquer pessoa com acesso à máquina/backup do arquivo tem a
credencial em texto puro. Usar variáveis de ambiente (já suportado pelo
`.env` / `.env.example` existentes na raiz do projeto) elimina a necessidade
de qualquer segredo literal dentro do código versionável.

### Descoberta adicional durante a investigação

Ao investigar o alcance do vazamento, foi confirmado via
`git log --all -S"<segredo>"` que:

- A senha de app do Gmail e o e-mail **já estiveram commitados no histórico**
  do Git, nos commits `283c870`, `c8e1697` e `6ab790a` — ou seja, mesmo que o
  arquivo de trabalho atual esteja limpo, o segredo **continua recuperável**
  por qualquer pessoa com acesso ao histórico do repositório (inclusive no
  GitHub remoto, já que o branch `main` está sincronizado com `origin/main`).
- As chaves JWT `private.pem` / `public.pem` também foram encontradas
  commitadas no histórico (commits `0593299` e `86f9ebb`). Isso não estava
  no escopo original do item C1 do README, mas é o mesmo tipo de risco
  (segredo em texto puro no histórico do Git) e deve ser tratado com a
  mesma prioridade.

### Pendências que exigem ação humana (não realizadas nesta sessão)

Estas ações não foram executadas automaticamente porque envolvem
credenciais de conta pessoal do usuário e/ou reescrita destrutiva do
histórico do Git compartilhado (exige `push --force` e coordenação com
qualquer outro colaborador/clone):

1. **Revogar imediatamente** a App Password do Gmail vazada em
   https://myaccount.google.com/apppasswords.
2. Gerar uma nova App Password e atualizar **apenas** o `.env` local
   (nunca commitado — já está no `.gitignore`).
3. Rotacionar o par de chaves JWT (`private.pem` / `public.pem`), pois o
   par atual está exposto no histórico do Git.
4. Limpar o histórico do Git com `git filter-repo` ou BFG Repo-Cleaner
   para remover essas strings/arquivos definitivamente, e depois fazer
   `push --force` coordenado com a equipe.

---

## C2 — Endpoint público expondo dados médicos de pacientes

- **Status:** ✅ Resolvido
- **Data:** 2026-08-05

### O que foi alterado

- `src/main/java/br/com/api_mediway/consultation/ConsultationController.java`,
  método `getConsultationByStatus` (`GET /consultation/by-status`): adicionada
  a anotação:

  ```java
  @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MEDICO') or hasAuthority('SCOPE_CUIDADOR')")
  ```

### Por quê

Era o único endpoint do `ConsultationController` sem nenhuma checagem de
autorização, permitindo que qualquer requisição não autenticada listasse
consultas médicas de pacientes (dado sensível de saúde / PHI) filtrando por
status. As roles escolhidas seguem exatamente o mesmo padrão já usado no
endpoint irmão `GET /consultation/by-date`, que tem a mesma natureza
(listagem/consulta administrativa e clínica).

---

## C3 — Ausência total de validação de entrada nos controllers (`@Valid`)

- **Status:** ✅ Resolvido
- **Data:** 2026-08-06

### O que foi alterado

- Adicionada a dependência `spring-boot-starter-validation` ao `pom.xml` — sem ela,
  a anotação `@Valid` já usada em `MedicationController` não tinha nenhum validador no
  classpath e era um no-op silencioso.
- Adicionado `@Valid` em todos os parâmetros `@RequestBody` que ainda não tinham,
  nos controllers `AdminController`, `AuthController`, `CaregiverController`,
  `ConsultationController`, `DoctorController`, `ExamController`, `PatientController`
  e `VaccineController`.
- Adicionadas anotações Bean Validation (`@NotBlank`, `@NotNull`, `@Email`, `@Size`,
  `@Pattern`, `@Past`, `@FutureOrPresent`, `@PositiveOrZero`, `@NotEmpty`) em todos os
  DTOs de request correspondentes (`CreateAdminDto`, `CreateUserDTO`, `LoginRequestDTO`,
  `PasswordResetCodeDTO`, `PasswordResetConfirmDTO`, `PasswordResetRequestDTO`,
  `CaregiverRequestDTO`, `ConsultationRequestDTO`, `AddInfosDoctorDTO`,
  `ExamRequestDTO`, `MedicationRequestDTO`, `AddInfosPatientsDTO`, `VaccineRequestDTO`).
  Os campos exigidos em cada DTO seguem o que o entity/factory correspondente já
  tratava como obrigatório (ex.: colunas `nullable = false`, chaves estrangeiras
  usadas em lookups).
- Nos DTOs de **atualização parcial** (`UpdateConsultationDTO`, `UpdateExamDTO`,
  `UpdateDoctorInfosDTO`, `UpdatePatientInfosDTO`) — cujos services já tratam campo
  nulo como "não alterar esse campo" — só foram adicionadas validações de **formato**
  (`@Email`, `@FutureOrPresent`, `@PositiveOrZero`, `@Past`), nunca `@NotBlank`/`@NotNull`,
  já que essas restrições quebrariam a semântica de atualização parcial existente.

### Por quê

Nenhum `@RequestBody` tinha validação de formato, tamanho ou nulidade antes desta
correção (e o único `@Valid` existente não fazia nada, por falta do validador no
classpath). Isso permitia, por exemplo, cadastrar usuários com e-mail em formato
inválido, senha vazia, ou disparar `NullPointerException`/erro 500 em vez de 400
quando um campo obrigatório não era enviado. O projeto já tinha um
`GlobalExceptionHandle.handleValidation(MethodArgumentNotValidException)` pronto
para essas respostas — ele só nunca era acionado porque a validação nunca rodava de
fato.

### Verificação

`mvn clean compile` executado com sucesso após as mudanças. Não havia testes
automatizados cobrindo os controllers para rodar (ver item I5 do README).

---

## C4 — `ddl-auto=update` ativo sem ferramenta de migração de schema

- **Status:** ✅ Resolvido
- **Data:** 2026-08-06

### O que foi alterado

- Adicionadas as dependências `spring-boot-starter-flyway` e `flyway-mysql` ao `pom.xml`.
- Criada a migration `src/main/resources/db/migration/V1__baseline_schema.sql`, com o
  schema exato que o Hibernate já gerava para todas as entidades (capturado rodando a
  aplicação com `ddl-auto=create` contra uma instância MySQL 8.0 **descartável e
  isolada**, sem tocar no volume `docker/mysql_data` usado pelo ambiente de
  desenvolvimento do projeto).
- `spring.jpa.hibernate.ddl-auto` mudado de `update` para `validate` em
  `application.properties`: o Hibernate passa a apenas **conferir** que as entidades
  batem com o schema já migrado, nunca mais aplica DDL automaticamente.
- Adicionada a classe `FlywayEarlyMigrationInitializer`
  (`common/config/FlywayEarlyMigrationInitializer.java`), registrada via
  `src/main/resources/META-INF/spring.factories` como `ApplicationContextInitializer`.
  Ela roda o `Flyway.migrate()` manualmente, **antes** do contexto Spring subir e
  antes de qualquer bean ser criado. `spring.flyway.enabled=false` desativa a
  auto-configuração padrão do Flyway, que ficaria redundante.
- `spring.flyway.baseline-on-migrate=true` + `spring.flyway.baseline-version=1`:
  permite adotar o Flyway em bancos que já existem (criados anteriormente via
  `ddl-auto=update`, como o ambiente local do `docker-compose.yml`) sem tentar
  recriar tabelas que já estão lá — a própria `V1__baseline_schema.sql` é marcada
  como "já aplicada" nesse caso. Em um banco novo/vazio (CI, ambiente novo) o
  baseline é ignorado e a V1 roda normalmente, criando o schema do zero.

### Por quê

`ddl-auto=update` deixa o Hibernate alterar o schema de produção automaticamente a
cada subida da aplicação, sem histórico de versão e sem possibilidade de revisão
antes de aplicar — uma migração que renomeia ou muda o tipo de uma coluna pode
corromper dados sem aviso. Com Flyway, toda mudança de schema passa a ser um
arquivo versionado, revisável em PR, e `ddl-auto=validate` garante que a aplicação
recusa subir se as entidades Java e o schema do banco divergirem, em vez de tentar
"consertar" silenciosamente.

O `FlywayEarlyMigrationInitializer` manual (em vez de depender só da
auto-configuração padrão do Flyway) foi necessário porque, na versão do Spring Boot
usada neste projeto, a integração automática entre o Flyway e o JPA (feita via
`DatabaseInitializerDetector`, através dos módulos `spring-boot-flyway` e
`spring-boot-jpa`/`spring-boot-hibernate`) não garante que o Flyway rode antes do
Hibernate: em testes, o `EntityManagerFactory` era criado e tentava validar o schema
antes do `FlywayMigrationInitializer` ter a chance de migrar, e a aplicação falhava
ao subir mesmo com as migrations corretas (chegou a gerar inclusive uma dependência
circular ao tentar corrigir isso apenas com `@DependsOn`). Rodar o Flyway em um
`ApplicationContextInitializer` elimina esse problema de ordenação por completo,
pois essa etapa acontece antes de qualquer bean (inclusive o `EntityManagerFactory`)
ser registrado.

### Verificação

Testado de ponta a ponta contra instâncias MySQL 8.0 e RabbitMQ **descartáveis**
(containers Docker isolados, sem volume nomeado, removidos ao final — em nenhum
momento o volume `docker/mysql_data` do ambiente de desenvolvimento do projeto foi
tocado):

1. **Banco novo/vazio:** Flyway aplica a `V1__baseline_schema.sql` do zero, Hibernate
   valida com sucesso, aplicação sobe normalmente.
2. **Banco já existente** (schema pré-criado simulando um ambiente que já rodava com
   `ddl-auto=update`): Flyway faz o baseline na V1 em vez de tentar recriar as
   tabelas, Hibernate valida com sucesso, aplicação sobe normalmente.

### Pendência que exige atenção do time

O campo `Medication.dias` (`List<String>`, sem `@ElementCollection`/`@Convert`
explícito) é mapeado pelo Hibernate como uma coluna `json` no MySQL — isso já
acontecia antes desta mudança (era assim que `ddl-auto=update` já criava a coluna),
mas agora que o schema está fixado em uma migration versionada, qualquer alteração
nesse campo (tipo, anotações) exigirá uma nova migration explícita em vez de deixar
o Hibernate ajustar a coluna sozinho — o que é exatamente o comportamento que o
Flyway deveria trazer.

---

<!--
Modelo para novas entradas:

## <ID> — <título curto>

- **Status:** ⬜ Não iniciado / 🟡 Em andamento / ✅ Resolvido





### O que foi alterado
- ...

### Por quê
- ...
-->