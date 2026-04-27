package br.com.fiap.report.domain.model;

import java.util.List;

public record Risco(
        Severidade severidade,
        CategoriaRisco categoria,
        String titulo,
        String descricao,
        List<String> componentesAfetados
) {}
