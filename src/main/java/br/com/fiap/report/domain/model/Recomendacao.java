package br.com.fiap.report.domain.model;

import java.util.List;

public record Recomendacao(
        Prioridade prioridade,
        String titulo,
        String descricao,
        List<String> referencias
) {}
