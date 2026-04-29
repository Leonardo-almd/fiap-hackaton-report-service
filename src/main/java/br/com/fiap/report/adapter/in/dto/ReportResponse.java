package br.com.fiap.report.adapter.in.dto;

import br.com.fiap.report.domain.model.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReportResponse(
        UUID reportId,
        UUID jobId,
        Instant createdAt,
        List<ComponenteResponse> componentes,
        List<RiscoResponse> riscos,
        List<RecomendacaoResponse> recomendacoes
) {
    public record ComponenteResponse(String nome, String tipo, String descricao) {}
    public record RiscoResponse(String severidade, String categoria, String titulo,
                                String descricao, List<String> componentesAfetados) {}
    public record RecomendacaoResponse(String prioridade, String titulo,
                                       String descricao, List<String> referencias) {}

    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getJobId(),
                report.getCreatedAt(),
                report.getComponentes().stream()
                        .map(c -> new ComponenteResponse(c.nome(), c.tipo().name(), c.descricao()))
                        .toList(),
                report.getRiscos().stream()
                        .map(r -> new RiscoResponse(r.severidade().name(), r.categoria().name(),
                                r.titulo(), r.descricao(), r.componentesAfetados()))
                        .toList(),
                report.getRecomendacoes().stream()
                        .map(rec -> new RecomendacaoResponse(rec.prioridade().name(),
                                rec.titulo(), rec.descricao(), rec.referencias()))
                        .toList()
        );
    }
}
