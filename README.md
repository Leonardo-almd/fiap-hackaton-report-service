# fiap-report-service

Microsserviço responsável por:
- Persistir relatórios de análise arquitetural gerados pela IA
- Expor relatórios para consulta pelo cliente via REST

**Responsável**: Pessoa 2

## Contrato de API

Documentação completa: [`fiap-infrastructure/docs/api/report-service-api.yaml`](https://github.com/org/fiap-infrastructure/blob/main/docs/api/report-service-api.yaml)

### Endpoints

| Método | Path | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/v1/reports/{jobId}` | Buscar relatório por jobId | Público (via API Gateway) |
| `POST` | `/v1/reports` | Criar relatório | Interno (processing-service apenas) |

### Swagger UI (local)

http://localhost:8082/swagger-ui.html

## Estrutura (Arquitetura Hexagonal)

```
src/main/java/br/com/fiap/report/
├── domain/
│   ├── model/          # Report, Componente, Risco, Recomendacao
│   └── port/
│       ├── in/         # GetReportUseCase, CreateReportUseCase
│       └── out/        # ReportRepository
├── application/
│   └── usecase/        # GetReportUseCaseImpl, CreateReportUseCaseImpl
├── adapter/
│   ├── in/
│   │   ├── web/        # ReportController
│   │   └── dto/        # CreateReportRequest, ReportResponse
│   └── out/
│       └── persistence/ # ReportJpaRepository, ReportEntity, ComponenteEntity, ...
└── config/             # OpenApiConfig
```

## Desenvolvimento local

```bash
# Pré-requisito: report-db rodando (ver fiap-infrastructure)
mvn spring-boot:run

mvn test
```

## Variáveis de ambiente

| Variável | Padrão (local) | Descrição |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5433/report_db` | URL do banco |
| `SPRING_DATASOURCE_USERNAME` | `report_user` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | `report_pass` | Senha do banco |
