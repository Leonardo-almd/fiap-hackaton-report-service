package br.com.fiap.report.domain.port.in;

import br.com.fiap.report.domain.model.*;

import java.util.List;
import java.util.UUID;

public interface CreateReportUseCase {

    record Command(
            UUID jobId,
            List<Componente> componentes,
            List<Risco> riscos,
            List<Recomendacao> recomendacoes
    ) {}

    Report execute(Command command);
}
