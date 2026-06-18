# Notificação

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen) ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-template-yellowgreen) ![JavaMail](https://img.shields.io/badge/JavaMail-SMTP-blue) ![Gradle](https://img.shields.io/badge/Gradle-8.14-blue) ![Docker](https://img.shields.io/badge/Docker-ready-informational)

Microsserviço responsável pelo envio de e-mails de notificação referentes a tarefas agendadas. Recebe os dados da tarefa, monta o conteúdo a partir de um template **Thymeleaf** e realiza o envio via **SMTP**. É consumido pelo serviço **bff-agendador-tarefas**, que dispara as notificações através de um job agendado.

## Sumário

- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Endpoints](#endpoints)
- [Modelo de Dados](#modelo-de-dados)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Relacionamento com outros serviços](#relacionamento-com-outros-serviços)
- [Autor](#autor)

## Funcionalidades

- Envio de e-mail de notificação referente a uma tarefa agendada
- Geração do corpo do e-mail a partir de template **Thymeleaf**, com nome da tarefa, descrição e data do evento
- Tratamento de exceções de envio (`EmailException`)

## Tecnologias

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Boot Starter Mail** (JavaMailSender)
- **Thymeleaf** (templates de e-mail)
- **Spring Web MVC**
- **Lombok**
- **Gradle**
- **Docker**

## Arquitetura

```
src/
└── main/
    ├── java/
    │   └── com.morcineck.notificacao/
    │       ├── business/             # EmailService e DTOs
    │       ├── controller/           # Endpoint REST
    │       └── infrastructure/       # Exceptions
    └── resources/
        └── templates/                # Templates Thymeleaf (notificacao.html)
```

## Endpoints

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|----------------|
| `POST` | `/email` | Envia um e-mail de notificação referente a uma tarefa | Não |

## Modelo de Dados

Payload recebido para o envio do e-mail:

```json
{
  "id": "string",
  "nomeTarefa": "string",
  "descricao": "string",
  "dataCriacao": "dd-MM-yyyy HH:mm:ss",
  "dataEvento": "dd-MM-yyyy HH:mm:ss",
  "emailUsuario": "string",
  "dataAlteracao": "dd-MM-yyyy HH:mm:ss",
  "statusNotificacaoEnum": "pendente | notificado | cancelado"
}
```

O e-mail é enviado para o endereço informado em `emailUsuario`, utilizando `nomeTarefa`, `descricao` e `dataEvento` como variáveis no template.

## Variáveis de Ambiente

| Variável | Descrição |
|----------|-----------|
| `MAIL_USERNAME` | E-mail utilizado para autenticação no servidor SMTP |
| `MAIL_PASSWORD` | Senha de aplicativo do e-mail remetente |

> As credenciais de e-mail **nunca** devem ser commitadas no repositório. Utilize variáveis de ambiente ou um arquivo `.env` ignorado pelo Git.

## Como Executar

Este repositório faz parte do sistema de Agendador de Tarefas, orquestrado via Docker Compose junto aos demais microsserviços:

```bash
docker-compose up --build
```

A aplicação ficará disponível em `http://localhost:8082`.

## Testes

```bash
./gradlew test
```

## Relacionamento com outros serviços

Este serviço é consumido pelo **bff-agendador-tarefas**, responsável por verificar tarefas pendentes e disparar as notificações por e-mail através deste microsserviço.

```
        ┌──────────────────────────┐
        │  bff-agendador-tarefas   │
        └────────────┬─────────────┘
                     │
       ┌─────────────┼─────────────┐
       ▼             ▼             ▼
   usuario     agendador-     notificacao
               tarefas
```
