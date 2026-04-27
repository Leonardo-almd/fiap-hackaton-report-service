package br.com.fiap.report.application.usecase;

import br.com.fiap.report.domain.model.Report;
import br.com.fiap.report.domain.port.in.GetReportUseCase;
import br.com.fiap.report.domain.port.out.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class GetReportUseCaseImpl implements GetReportUseCase {

    private final ReportRepository reportRepository;

    public GetReportUseCaseImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report execute(UUID jobId) {
        return reportRepository.findByJobId(jobId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Relatório não encontrado para jobId: " + jobId));
    }
}
