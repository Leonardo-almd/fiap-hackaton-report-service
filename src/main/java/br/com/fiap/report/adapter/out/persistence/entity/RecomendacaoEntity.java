package br.com.fiap.report.adapter.out.persistence.entity;

import br.com.fiap.report.domain.model.Prioridade;
import br.com.fiap.report.domain.model.Recomendacao;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recomendacoes")
public class RecomendacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private ReportEntity report;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Prioridade prioridade;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    @Column(columnDefinition = "text[]")
    private String[] referencias;

    protected RecomendacaoEntity() {}

    public static RecomendacaoEntity from(Recomendacao r, ReportEntity report) {
        RecomendacaoEntity e = new RecomendacaoEntity();
        e.report = report;
        e.prioridade = r.prioridade();
        e.titulo = r.titulo();
        e.descricao = r.descricao();
        e.referencias = r.referencias() != null
                ? r.referencias().toArray(new String[0])
                : new String[0];
        return e;
    }

    public Recomendacao toDomain() {
        return new Recomendacao(prioridade, titulo, descricao,
                referencias != null ? List.of(referencias) : List.of());
    }
}
