package aep5s.zonaverde.repository;

import aep5s.zonaverde.domain.entity.EspacoPublico;
import aep5s.zonaverde.domain.enums.TipoEspaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EspacoPublicoRepository extends JpaRepository<EspacoPublico, Long> {
    List<EspacoPublico> findByTipo(TipoEspaco tipo);
}
