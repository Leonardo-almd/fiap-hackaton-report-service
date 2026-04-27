package br.com.fiap.report.adapter.in.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateReportRequest(
        @NotNull UUID jobId,
        @NotNull @Valid List<ComponenteDto> componentes,
        @NotNull @Valid List<RiscoDto> riscos,
        @NotNull @Valid List<RecomendacaoDto> recomendacoes
) {
    public record ComponenteDto(
            @NotNull String nome,
            @NotNull String tipo,
            @NotNull String descricao
    ) {}

    public record RiscoDto(
            @NotNull String severidade,
            @NotNull String categoria,
            @NotNull String titulo,
            @NotNull String descricao,
            List<String> componentesAfetados
    ) {}

    public record RecomendacaoDto(
            @NotNull String prioridade,
            @NotNull String titulo,
            @NotNull String descricao,
            List<String> referencias
    ) {}
}
