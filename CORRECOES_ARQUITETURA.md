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
| C3 | Ausência de `@Valid` nos controllers | ⬜ Não iniciado | — |
| C4 | `ddl-auto=update` sem Flyway/Liquibase | ⬜ Não iniciado | — |
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

<!--
Modelo para novas entradas:

## <ID> — <título curto>

- **Status:** ⬜ Não iniciado / 🟡 Em andamento / ✅ Resolvido
- **Data:** AAAA-MM-DD

### O que foi alterado
- ...

### Por quê
- ...
-->