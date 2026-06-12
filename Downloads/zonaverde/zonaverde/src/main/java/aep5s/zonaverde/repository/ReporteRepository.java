package aep5s.zonaverde.repository;

import aep5s.zonaverde.domain.entity.Reporte;
import aep5s.zonaverde.domain.enums.TipoEspaco;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByEspacoPublicoId(Long espacoId);
}
