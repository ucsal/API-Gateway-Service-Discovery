# service-discovery

Servidor Eureka responsável por registrar e permitir a descoberta dos microserviços do sistema.

## Como executar

```bash
mvn spring-boot:run
```

Acesse:

```text
http://localhost:8761
```

## Observação

Todos os microserviços e o API Gateway devem apontar para:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```
