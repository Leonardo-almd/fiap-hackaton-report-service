-- Reports
CREATE TABLE IF NOT EXISTS reports (
    id         UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    job_id     UUID        NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_reports_job_id ON reports (job_id);

-- Componentes
CREATE TABLE IF NOT EXISTS componentes (
    id         BIGSERIAL   PRIMARY KEY,
    report_id  UUID        NOT NULL REFERENCES reports(id) ON DELETE CASCADE,
    nome       VARCHAR(200) NOT NULL,
    tipo       VARCHAR(50)  NOT NULL
                    CHECK (tipo IN ('GATEWAY','SERVICE','DATABASE','QUEUE','CACHE',
                                    'CDN','LOAD_BALANCER','STORAGE','CLIENT','OTHER','EXTERNAL','UNKNOWN')),
    descricao  TEXT        NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_componentes_report_id ON componentes (report_id);

-- Riscos
CREATE TABLE IF NOT EXISTS riscos (
    id                   BIGSERIAL   PRIMARY KEY,
    report_id            UUID        NOT NULL REFERENCES reports(id) ON DELETE CASCADE,
    severidade           VARCHAR(10)  NOT NULL CHECK (severidade IN ('ALTA','MEDIA','BAIXA')),
    categoria            VARCHAR(30)  NOT NULL
                             CHECK (categoria IN ('SEGURANCA','ACOPLAMENTO','ESCALABILIDADE',
                                                  'DISPONIBILIDADE', 'OBSERVABILIDADE','PERFORMANCE','MANUTENCAO', 'OTHER')),
    titulo               VARCHAR(200) NOT NULL,
    descricao            TEXT         NOT NULL,
    componentes_afetados TEXT[]       NOT NULL DEFAULT '{}'
);

CREATE INDEX IF NOT EXISTS idx_riscos_report_id ON riscos (report_id);

-- Recomendações
CREATE TABLE IF NOT EXISTS recomendacoes (
    id         BIGSERIAL    PRIMARY KEY,
    report_id  UUID         NOT NULL REFERENCES reports(id) ON DELETE CASCADE,
    prioridade VARCHAR(10)  NOT NULL CHECK (prioridade IN ('ALTA','MEDIA','BAIXA')),
    titulo     VARCHAR(200) NOT NULL,
    descricao  TEXT         NOT NULL,
    referencias TEXT[]      NOT NULL DEFAULT '{}'
);

CREATE INDEX IF NOT EXISTS idx_recomendacoes_report_id ON recomendacoes (report_id);
