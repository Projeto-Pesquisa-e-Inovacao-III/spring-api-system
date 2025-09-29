# 🔐 Spring Boot + JWT Authentication

Este projeto foi desenvolvido como back-end para a aplicação 
[react-app](https://github.com/Projeto-Pesquisa-e-Inovacao-III/react-app/tree/main),
utilizando autenticação via JWT (JSON Web Token) para garantir
segurança e integridade nas comunicações.

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