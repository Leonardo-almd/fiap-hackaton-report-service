package br.com.fiap.report.domain.port.out;

import br.com.fiap.report.domain.model.Report;

import java.util.Optional;
import java.util.UUID;

public interface ReportRepository {

    Report save(Report report);

    Optional<Report> findByJobId(UUID jobId);
}
