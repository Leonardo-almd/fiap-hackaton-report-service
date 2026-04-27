package br.com.fiap.report.unit;

import br.com.fiap.report.application.usecase.GetReportUseCaseImpl;
import br.com.fiap.report.domain.model.Report;
import br.com.fiap.report.domain.port.out.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetReportUseCaseTest {

    @Mock
    private ReportRepository reportRepository;

    private GetReportUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetReportUseCaseImpl(reportRepository);
    }

    @Test
    void shouldReturnReportWhenFound() {
        UUID jobId = UUID.randomUUID();
        Report report = new Report(UUID.randomUUID(), jobId,
                List.of(), List.of(), List.of(), Instant.now());

        when(reportRepository.findByJobId(jobId)).thenReturn(Optional.of(report));

        Report result = useCase.execute(jobId);

        assertThat(result).isNotNull();
        assertThat(result.getJobId()).isEqualTo(jobId);
    }

    @Test
    void shouldThrowWhenReportNotFound() {
        UUID jobId = UUID.randomUUID();
        when(reportRepository.findByJobId(jobId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(jobId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(jobId.toString());
    }
}
