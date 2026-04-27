package br.com.fiap.report.adapter.out.persistence.entity;

import br.com.fiap.report.domain.model.Componente;
import br.com.fiap.report.domain.model.TipoComponente;
import jakarta.persistence.*;

@Entity
@Table(name = "componentes")
public class ComponenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private ReportEntity report;

    @Column(nullable = false, length = 200)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoComponente tipo;

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    protected ComponenteEntity() {}

    public static ComponenteEntity from(Componente c, ReportEntity report) {
        ComponenteEntity e = new ComponenteEntity();
        e.report = report;
        e.nome = c.nome();
        e.tipo = c.tipo();
        e.descricao = c.descricao();
        return e;
    }

    public Componente toDomain() {
        return new Componente(nome, tipo, descricao);
    }
}
