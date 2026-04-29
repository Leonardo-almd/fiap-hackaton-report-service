package br.com.fiap.report.adapter.out.persistence;

import br.com.fiap.report.adapter.out.persistence.entity.ReportEntity;
import br.com.fiap.report.adapter.out.persistence.repository.ReportJpaRepository;
import br.com.fiap.report.domain.model.Report;
import br.com.fiap.report.domain.port.out.ReportRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ReportRepositoryAdapter implements ReportRepository {

    private final ReportJpaRepository jpaRepository;

    public ReportRepositoryAdapter(ReportJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Report save(Report report) {
        return jpaRepository.save(ReportEntity.from(report)).toDomain();
    }

    @Override
    public Optional<Report> findByJobId(UUID jobId) {
        return jpaRepository.findByJobId(jobId).map(ReportEntity::toDomain);
    }
}
