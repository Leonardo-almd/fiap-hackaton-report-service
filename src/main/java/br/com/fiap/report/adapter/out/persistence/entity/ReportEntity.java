package br.com.fiap.report.adapter.out.persistence.entity;

import br.com.fiap.report.domain.model.*;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "reports")
public class ReportEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "job_id", nullable = false, unique = true, columnDefinition = "uuid")
    private UUID jobId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ComponenteEntity> componentes = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RiscoEntity> riscos = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecomendacaoEntity> recomendacoes = new ArrayList<>();

    protected ReportEntity() {}

    public static ReportEntity from(Report report) {
        ReportEntity entity = new ReportEntity();
        entity.id = report.getId();
        entity.jobId = report.getJobId();
        entity.createdAt = report.getCreatedAt();

        report.getComponentes().forEach(c -> {
            ComponenteEntity ce = ComponenteEntity.from(c, entity);
            entity.componentes.add(ce);
        });
        report.getRiscos().forEach(r -> {
            RiscoEntity re = RiscoEntity.from(r, entity);
            entity.riscos.add(re);
        });
        report.getRecomendacoes().forEach(rec -> {
            RecomendacaoEntity re = RecomendacaoEntity.from(rec, entity);
            entity.recomendacoes.add(re);
        });

        return entity;
    }

    public Report toDomain() {
        return new Report(
                id, jobId,
                componentes.stream().map(ComponenteEntity::toDomain).collect(Collectors.toList()),
                riscos.stream().map(RiscoEntity::toDomain).collect(Collectors.toList()),
                recomendacoes.stream().map(RecomendacaoEntity::toDomain).collect(Collectors.toList()),
                createdAt
        );
    }

    public UUID getId() { return id; }
    public UUID getJobId() { return jobId; }
}
