package br.com.fiap.report.adapter.out.persistence.entity;

import br.com.fiap.report.domain.model.CategoriaRisco;
import br.com.fiap.report.domain.model.Risco;
import br.com.fiap.report.domain.model.Severidade;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "riscos")
public class RiscoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private ReportEntity report;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Severidade severidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaRisco categoria;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    @Column(name = "componentes_afetados", columnDefinition = "text[]")
    private String[] componentesAfetados;

    protected RiscoEntity() {}

    public static RiscoEntity from(Risco r, ReportEntity report) {
        RiscoEntity e = new RiscoEntity();
        e.report = report;
        e.severidade = r.severidade();
        e.categoria = r.categoria();
        e.titulo = r.titulo();
        e.descricao = r.descricao();
        e.componentesAfetados = r.componentesAfetados() != null
                ? r.componentesAfetados().toArray(new String[0])
                : new String[0];
        return e;
    }

    public Risco toDomain() {
        return new Risco(severidade, categoria, titulo, descricao,
                componentesAfetados != null ? List.of(componentesAfetados) : List.of());
    }
}
