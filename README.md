# 🔐 Spring Boot + JWT Authentication

Este projeto foi desenvolvido como back-end para a aplicação 
[react-app](https://github.com/Projeto-Pesquisa-e-Inovacao-III/react-app/tree/main),
utilizando autenticação via JWT (JSON Web Token) para garantir
segurança e integridade nas comunicações.

---

## 📑 Índice

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Como Utilizar](#-como-utilizar)
    - [1️⃣ Configurar Variáveis de Ambiente](#1-configurar-variáveis-de-ambiente)
    - [2️⃣ Gerar a chave privada](#2-gerar-a-chave-privada)
    - [3️⃣ Gerar chave pública a partir da chave privada](#3-gerar-chave-pública-a-partir-da-chave-privada)
    - [4️⃣ Onde colocar as chaves](#4-onde-colocar-as-chaves)
    - [5️⃣ Execução](#5-execução)
- [Perfis de Ambiente](#-perfis-de-ambiente)
- [Documentação](#-documentação)

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security
- JWT (Auth0)

---

## ⚙️ Como Utilizar

### 1️⃣ Configurar Variáveis de Ambiente

O projeto utiliza variáveis de ambiente para configurações sensíveis. 

#### Para o ambiente de **Desenvolvimento** (profile: dev):

Copie o arquivo `.env` na raiz do projeto e preencha com suas credenciais:

```env
# Mail Configuration (Mailtrap for dev/testing)
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password

# Infobip Configuration
INFOBIP_API_KEY=your_infobip_api_key_here
INFOBIP_WHATSAPP_SENDER=your_whatsapp_sender_number

# Discord Webhook
DISCORD_WEBHOOK_URL=your_discord_webhook_url_here
```

#### Para o ambiente de **Produção** (profile: prod):

Configure as seguintes variáveis de ambiente no sistema ou no servidor:

```env
# Server
SERVER_PORT=8080

# Database (MySQL)
DB_URL=jdbc:mysql://localhost:3306/spring_api_system?useSSL=false&serverTimezone=UTC
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
DDL_AUTO=update

# Mail (Production SMTP)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Infobip
INFOBIP_API_KEY=your_infobip_api_key
INFOBIP_WHATSAPP_SENDER=your_whatsapp_sender_number

# Discord
DISCORD_WEBHOOK_URL=your_discord_webhook_url

# Storage
STORAGE_DIR=imagens
```

#### Para o ambiente **Docker** (profile: docker):

Use o arquivo `.env.docker.example` como referência para configurar suas variáveis.

---

Como o projeto utiliza JWT com assinatura assimétrica, 
é necessário gerar um par de chaves RSA. Siga os passos abaixo:

### 2️⃣ Gerar a chave privada
```
openssl genrsa -out private.pem 2048
```

### 3️⃣ Gerar chave pública a partir da chave privada
```
openssl rsa -in private.pem -pubout -out public.pem
```

### 4️⃣ Onde colocar as chaves

Agora basta abrir o projeto e seguir o exemplo abaixo:

```
spring-api-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── keys/
│   │           ├── private.pem
│   │           └── public.pem           
```

### 5️⃣ Execução

Com tudo configurado, chegou a hora de rodar a aplicação!

Localize a classe `ApiSystemApplication.java`, que está no pacote principal do projeto, e execute-a.
Essa é a classe responsável por iniciar o Spring Boot.

Pronto! Sua API estará disponível e pronta para receber requisições.

---

## 🌍 Perfis de Ambiente

O projeto suporta diferentes perfis de ambiente:

### 📘 dev (Desenvolvimento)
- Usa banco de dados **H2** em memória
- Console H2 habilitado em `/h2-console`
- SQL de inicialização executado automaticamente (`data.sql`)
- Logs detalhados habilitados
- Ideal para desenvolvimento local

### 📗 prod (Produção)
- Usa banco de dados **MySQL**
- H2 desabilitado
- Requer todas as variáveis de ambiente configuradas
- Logs otimizados
- DDL auto configurável via `DDL_AUTO`

### 📙 docker (Docker)
- Configurado para rodar em containers
- Usa MySQL com hostname `mysql` (Docker Compose)
- Variáveis de ambiente obrigatórias
- H2 desabilitado

Para alterar o perfil ativo, edite o arquivo `application.yml`:
```yaml
spring:
  profiles:
    active: dev  # Altere para: dev, prod ou docker
```

Ou defina via variável de ambiente:
```bash
export SPRING_PROFILES_ACTIVE=dev
```

---

## 📚 Documentação

Durante a execução do ambiente, você pode acessar a documentação da API
diretamente pelo navegador, utilizando a URL abaixo:

```
http://localhost:8080/doc
```

Essa interface fornece uma visão completa dos endpoints disponíveis,
facilitando testes e integração com outras aplicações.