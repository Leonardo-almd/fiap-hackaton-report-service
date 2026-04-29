package br.com.fiap.report.application.usecase;

import br.com.fiap.report.domain.model.Report;
import br.com.fiap.report.domain.port.in.CreateReportUseCase;
import br.com.fiap.report.domain.port.out.ReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateReportUseCaseImpl implements CreateReportUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateReportUseCaseImpl.class);

    private final ReportRepository reportRepository;

    public CreateReportUseCaseImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    @Transactional
    public Report execute(Command command) {
        MDC.put("jobId", command.jobId().toString());
        log.info("Criando relatório. jobId={}, componentes={}, riscos={}, recomendacoes={}",
                command.jobId(),
                command.componentes().size(),
                command.riscos().size(),
                command.recomendacoes().size());

        Report report = Report.create(
                command.jobId(),
                command.componentes(),
                command.riscos(),
                command.recomendacoes()
        );

        Report saved = reportRepository.save(report);
        log.info("Relatório criado. reportId={}, jobId={}", saved.getId(), saved.getJobId());
        MDC.remove("jobId");
        return saved;
    }
}
