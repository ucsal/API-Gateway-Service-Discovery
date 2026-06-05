# api-gateway

API Gateway do sistema. Ele atua como entrada única para o consumo dos microserviços e também possui um exemplo de orquestração.

## Como executar

Antes, execute o `service-discovery` na porta 8761.

Depois rode:

```bash
mvn spring-boot:run
```

Gateway:

```text
http://localhost:8080
```

## Rotas configuradas

| Rota externa no Gateway | Serviço de destino |
|---|---|
| `/api/academico/**` | `ms-academico` |
| `/api/professores/**` | `ms-professor` |
| `/api/formacoes/**` | `ms-professor` |
| `/api/professor-portal/**` | `ms-professor` |
| `/api/disciplinas/**` | `ms-disciplina` |
| `/api/programas/**` | `ms-disciplina` |
| `/api/bibliografias/**` | `ms-bibliografia` |
| `/api/auth/**` | `ms-auth` |
| `/api/relatorios/**` | `ms-disciplina` |

## Endpoints próprios do Gateway

```http
GET /api/orquestrador/status
GET /api/orquestrador/instancias/{serviceId}
GET /api/orquestrador/programa-completo/{programaId}
```

O endpoint `/api/orquestrador/programa-completo/{programaId}` é um exemplo de composição por orquestração, pois o Gateway controla o fluxo e consulta mais de um microserviço.

## Configuração que cada microserviço deve ter

Cada microserviço do grupo precisa ter dependência de Eureka Client e esta configuração:

```yaml
spring:
  application:
    name: ms-academico # alterar conforme o microserviço

server:
  port: 0 # ou uma porta fixa, exemplo 8081

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

Nomes esperados pelo Gateway:

```text
ms-academico
ms-professor
ms-disciplina
ms-bibliografia
ms-auth
```

Se o grupo usar outros nomes, altere o `application.yml` do Gateway.
