# Projetos da parte API Gateway + Service Discovery


1. `service-discovery`
2. `api-gateway`

## Ordem de execução

### 1. Executar o Service Discovery

```bash
cd service-discovery
mvn spring-boot:run
```

Abrir:

```text
http://localhost:8761
```

### 2. Executar o API Gateway

```bash
cd api-gateway
mvn spring-boot:run
```

Testar:

```text
http://localhost:8080/api/orquestrador/status
```



