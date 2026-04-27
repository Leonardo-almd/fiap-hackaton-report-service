package br.com.fiap.report.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Report {

    private final UUID id;
    private final UUID jobId;
    private final List<Componente> componentes;
    private final List<Risco> riscos;
    private final List<Recomendacao> recomendacoes;
    private final Instant createdAt;

    public Report(UUID id, UUID jobId, List<Componente> componentes,
                  List<Risco> riscos, List<Recomendacao> recomendacoes, Instant createdAt) {
        this.id = id;
        this.jobId = jobId;
        this.componentes = componentes;
        this.riscos = riscos;
        this.recomendacoes = recomendacoes;
        this.createdAt = createdAt;
    }

    public static Report create(UUID jobId, List<Componente> componentes,
                                List<Risco> riscos, List<Recomendacao> recomendacoes) {
        return new Report(UUID.randomUUID(), jobId, componentes, riscos, recomendacoes, Instant.now());
    }

    public UUID getId() { return id; }
    public UUID getJobId() { return jobId; }
    public List<Componente> getComponentes() { return componentes; }
    public List<Risco> getRiscos() { return riscos; }
    public List<Recomendacao> getRecomendacoes() { return recomendacoes; }
    public Instant getCreatedAt() { return createdAt; }
}
