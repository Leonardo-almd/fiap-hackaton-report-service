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

## Como rodar com Docker Compose

Este serviço eh executado via `docker compose` no repositório `fiap-infrastructure`.
O Compose sobe para este serviço:
- `report-db` (PostgreSQL)
- `report-service` (esta API, usando este `Dockerfile`)

Opcionalmente, para fluxo ponta a ponta, rode junto com `upload-service` e `processing-service`.

### 1) Pre-requisitos

- Docker Desktop ativo
- Docker Compose v2 (`docker compose version`)
- Estrutura multi-repo em pastas irmãs:

```text
Hackaton/
├── fiap-upload-service/
├── fiap-processing-service/
├── fiap-report-service/
└── fiap-infrastructure/
```

### 2) Subir banco e API de relatórios

No diretório `fiap-infrastructure`:

```bash
cd ../fiap-infrastructure
docker compose up -d report-db report-service
```

### 3) Verificar se subiu corretamente

```bash
docker compose ps
```

Estado esperado:
- `fiap-report-db`: `healthy`
- `fiap-report-service`: `healthy` (porta `8082:8082`)

Healthcheck da API:

```bash
curl http://localhost:8082/actuator/health
```

Resposta esperada:

```json
{
  "status": "UP"
}
```

Swagger local:
- http://localhost:8082/swagger-ui.html

### 4) Teste rapido de uso (simulando chamadas)

Criar relatório (endpoint interno):

```bash
JOB_ID=$(python3 -c 'import uuid; print(str(uuid.uuid4()))')

curl -X POST "http://localhost:8082/v1/reports" \
  -H "Content-Type: application/json" \
  -d "{
    \"jobId\": \"$JOB_ID\",
    \"componentes\": [
      {\"nome\":\"API Gateway\",\"tipo\":\"GATEWAY\",\"descricao\":\"Ponto de entrada\"}
    ],
    \"riscos\": [
      {\"severidade\":\"ALTA\",\"categoria\":\"ACOPLAMENTO\",\"titulo\":\"Banco compartilhado\",\"descricao\":\"Risco de acoplamento\",\"componentesAfetados\":[\"API Gateway\"]}
    ],
    \"recomendacoes\": [
      {\"prioridade\":\"ALTA\",\"titulo\":\"Separar bancos\",\"descricao\":\"Adotar database per service\",\"referencias\":[]}
    ]
  }"
```

Consultar relatório por `jobId`:

```bash
curl "http://localhost:8082/v1/reports/$JOB_ID"
```

### 5) Rebuild da API após alterar código

No `fiap-infrastructure`:

```bash
docker compose up -d --build report-service
```

### 6) Parar ambiente

```bash
docker compose stop report-service report-db
```

Reset completo:

```bash
docker compose down -v --remove-orphans
```

### 7) Troubleshooting

#### Erro de Flyway ao iniciar report-service

Sintoma:
- serviço inicia e cai durante bootstrap

Solução:

```bash
cd ../fiap-infrastructure
docker compose down -v --remove-orphans
docker compose up -d report-db report-service
```

#### Porta 8082 ocupada

Sintoma:
- erro de bind na porta 8082 ao iniciar container

Solução:

```bash
lsof -i :8082
# encerre o processo que estiver usando a porta
```

#### Confirmar que o Compose usa o Dockerfile correto

No `fiap-infrastructure/docker-compose.yml`:

```yaml
report-service:
  build:
    context: ../fiap-report-service
    dockerfile: Dockerfile
```

Ou seja, sim: o arquivo `fiap-report-service/Dockerfile` eh o usado pelo Compose.

## Variáveis de ambiente

| Variável | Padrão (local) | Descrição |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5433/report_db` | URL do banco |
| `SPRING_DATASOURCE_USERNAME` | `report_user` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | `report_pass` | Senha do banco |
