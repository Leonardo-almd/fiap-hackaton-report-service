package br.com.fiap.report.unit;

import br.com.fiap.report.adapter.in.web.ReportController;
import br.com.fiap.report.domain.model.*;
import br.com.fiap.report.domain.port.in.CreateReportUseCase;
import br.com.fiap.report.domain.port.in.GetReportUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean GetReportUseCase getReportUseCase;
    @MockitoBean CreateReportUseCase createReportUseCase;

    private Report sampleReport(UUID jobId) {
        return new Report(UUID.randomUUID(), jobId,
                List.of(new Componente("API GW", TipoComponente.GATEWAY, "Entry point")),
                List.of(new Risco(Severidade.ALTA, CategoriaRisco.ACOPLAMENTO,
                        "DB compartilhado", "Descrição", List.of())),
                List.of(new Recomendacao(Prioridade.ALTA, "Separar DBs", "Detalhes", List.of())),
                Instant.now());
    }

    @Test
    void getReport_shouldReturn200WhenFound() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(getReportUseCase.execute(jobId)).thenReturn(sampleReport(jobId));

        mockMvc.perform(get("/v1/reports/{jobId}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobId").value(jobId.toString()))
                .andExpect(jsonPath("$.componentes[0].nome").value("API GW"))
                .andExpect(jsonPath("$.riscos[0].severidade").value("ALTA"))
                .andExpect(jsonPath("$.recomendacoes[0].prioridade").value("ALTA"));
    }

    @Test
    void getReport_shouldReturn404WhenNotFound() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(getReportUseCase.execute(jobId)).thenThrow(new NoSuchElementException("não encontrado"));

        mockMvc.perform(get("/v1/reports/{jobId}", jobId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createReport_shouldReturn201() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(createReportUseCase.execute(any())).thenReturn(sampleReport(jobId));

        var body = Map.of(
                "jobId", jobId.toString(),
                "componentes", List.of(Map.of("nome", "API GW", "tipo", "GATEWAY", "descricao", "Entry point")),
                "riscos", List.of(Map.of("severidade", "ALTA", "categoria", "ACOPLAMENTO",
                        "titulo", "DB", "descricao", "Desc", "componentesAfetados", List.of())),
                "recomendacoes", List.of(Map.of("prioridade", "ALTA", "titulo", "T",
                        "descricao", "D", "referencias", List.of()))
        );

        mockMvc.perform(post("/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobId").value(jobId.toString()));
    }

    @Test
    void createReport_shouldReturn400WhenJobIdMissing() throws Exception {
        var body = Map.of(
                "componentes", List.of(),
                "riscos", List.of(),
                "recomendacoes", List.of()
        );

        mockMvc.perform(post("/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
