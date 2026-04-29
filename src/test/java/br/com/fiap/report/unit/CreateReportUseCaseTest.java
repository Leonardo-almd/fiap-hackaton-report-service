package br.com.fiap.report.unit;

import br.com.fiap.report.application.usecase.CreateReportUseCaseImpl;
import br.com.fiap.report.domain.model.*;
import br.com.fiap.report.domain.port.in.CreateReportUseCase;
import br.com.fiap.report.domain.port.out.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateReportUseCaseTest {

    @Mock
    private ReportRepository reportRepository;

    private CreateReportUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateReportUseCaseImpl(reportRepository);
    }

    @Test
    void shouldCreateAndPersistReport() {
        UUID jobId = UUID.randomUUID();
        var componentes = List.of(new Componente("API GW", TipoComponente.GATEWAY, "Entry point"));
        var riscos = List.of(new Risco(Severidade.ALTA, CategoriaRisco.ACOPLAMENTO,
                "DB compartilhado", "Descrição", List.of("Serviço A")));
        var recomendacoes = List.of(new Recomendacao(Prioridade.ALTA,
                "Separar DBs", "Detalhes", List.of()));

        when(reportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CreateReportUseCase.Command command = new CreateReportUseCase.Command(
                jobId, componentes, riscos, recomendacoes);

        Report result = useCase.execute(command);

        assertThat(result).isNotNull();
        assertThat(result.getJobId()).isEqualTo(jobId);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getComponentes()).hasSize(1);
        assertThat(result.getRiscos()).hasSize(1);
        assertThat(result.getRecomendacoes()).hasSize(1);

        verify(reportRepository).save(any(Report.class));
    }
}
