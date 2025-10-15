# 🔐 Spring Boot + JWT Authentication

Este projeto foi desenvolvido como back-end para a aplicação 
[react-app](https://github.com/Projeto-Pesquisa-e-Inovacao-III/react-app/tree/main),
utilizando autenticação via JWT (JSON Web Token) para garantir
segurança e integridade nas comunicações.

---

## 📑 Índice

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Como Utilizar](#-como-utilizar)
    - [1️⃣ Gerar a chave privada](#1-gerar-a-chave-privada)
    - [2️⃣ Gerar chave pública a partir da chave privada](#2-gerar-chave-pública-a-partir-da-chave-privada)
    - [3️⃣ Onde colocar as chaves](#3-onde-colocar-as-chaves)
    - [4️⃣ Execução](#4-execução)
- [Documentação](#-documentação)

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security
- JWT (Auth0)

---

## ⚙️ Como Utilizar

Como o projeto utiliza JWT com assinatura assimétrica, 
é necessário gerar um par de chaves RSA. Siga os passos abaixo:

### 1️⃣ Gerar a chave privada
```
openssl genrsa -out private.pem 2048
```

### 2️⃣ Gerar chave pública a partir da chave privada
```
openssl rsa -in private.pem -pubout -out public.pem
```

### 3️⃣ Onde colocar as chaves

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

### 4️⃣ Execução

Com tudo configurado, chegou a hora de rodar a aplicação!

Localize a classe `ApiSystemApplication.java`, que está no pacote principal do projeto, e execute-a.
Essa é a classe responsável por iniciar o Spring Boot.

Pronto! Sua API estará disponível e pronta para receber requisições.

---

## 📚 Documentação

Durante a execução do ambiente, você pode acessar a documentação da API
diretamente pelo navegador, utilizando a URL abaixo:

```
http://localhost:8080/doc
```

Essa interface fornece uma visão completa dos endpoints disponíveis,
facilitando testes e integração com outras aplicações.