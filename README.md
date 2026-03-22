
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