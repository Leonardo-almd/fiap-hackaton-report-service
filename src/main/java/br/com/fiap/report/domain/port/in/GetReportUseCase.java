package br.com.fiap.report.domain.port.in;

import br.com.fiap.report.domain.model.Report;

import java.util.UUID;

public interface GetReportUseCase {

    Report execute(UUID jobId);
}
