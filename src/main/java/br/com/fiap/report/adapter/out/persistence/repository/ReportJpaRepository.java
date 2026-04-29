package br.com.fiap.report.adapter.out.persistence.repository;

import br.com.fiap.report.adapter.out.persistence.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReportJpaRepository extends JpaRepository<ReportEntity, UUID> {

    Optional<ReportEntity> findByJobId(UUID jobId);
}
