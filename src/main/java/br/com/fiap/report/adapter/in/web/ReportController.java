package br.com.fiap.report.adapter.in.web;

import br.com.fiap.report.adapter.in.dto.CreateReportRequest;
import br.com.fiap.report.adapter.in.dto.ReportResponse;
import br.com.fiap.report.domain.model.*;
import br.com.fiap.report.domain.port.in.CreateReportUseCase;
import br.com.fiap.report.domain.port.in.GetReportUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/reports")
@Tag(name = "Reports", description = "Relatórios de análise arquitetural")
public class ReportController {

    private final GetReportUseCase getReportUseCase;
    private final CreateReportUseCase createReportUseCase;

    public ReportController(GetReportUseCase getReportUseCase,
                            CreateReportUseCase createReportUseCase) {
        this.getReportUseCase = getReportUseCase;
        this.createReportUseCase = createReportUseCase;
    }

    @GetMapping("/{jobId}")
    @Operation(summary = "Buscar relatório de análise por jobId")
    public ResponseEntity<ReportResponse> getReport(@PathVariable UUID jobId) {
        return ResponseEntity.ok(ReportResponse.from(getReportUseCase.execute(jobId)));
    }

    @PostMapping
    @Operation(summary = "Criar relatório de análise (uso interno — processing-service)")
    public ResponseEntity<ReportResponse> createReport(@Valid @RequestBody CreateReportRequest request) {
        var command = new CreateReportUseCase.Command(
                request.jobId(),
                mapComponentes(request.componentes()),
                mapRiscos(request.riscos()),
                mapRecomendacoes(request.recomendacoes())
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReportResponse.from(createReportUseCase.execute(command)));
    }

    private List<Componente> mapComponentes(List<CreateReportRequest.ComponenteDto> dtos) {
        return dtos.stream()
                .map(d -> new Componente(d.nome(), parseTipo(d.tipo()), d.descricao()))
                .toList();
    }

    private List<Risco> mapRiscos(List<CreateReportRequest.RiscoDto> dtos) {
        return dtos.stream()
                .map(d -> new Risco(
                        Severidade.valueOf(d.severidade()),
                        CategoriaRisco.valueOf(d.categoria()),
                        d.titulo(), d.descricao(),
                        d.componentesAfetados() != null ? d.componentesAfetados() : List.of()))
                .toList();
    }

    private List<Recomendacao> mapRecomendacoes(List<CreateReportRequest.RecomendacaoDto> dtos) {
        return dtos.stream()
                .map(d -> new Recomendacao(
                        Prioridade.valueOf(d.prioridade()),
                        d.titulo(), d.descricao(),
                        d.referencias() != null ? d.referencias() : List.of()))
                .toList();
    }

    private TipoComponente parseTipo(String tipo) {
        try {
            return TipoComponente.valueOf(tipo);
        } catch (IllegalArgumentException e) {
            return TipoComponente.UNKNOWN;
        }
    }
}
