package aep5s.zonaverde.repository;

import aep5s.zonaverde.domain.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByEspacoPublicoId(Long espacoId);

    @Query("SELECT e FROM Evento e WHERE e.espacoPublico.id = :espacoId " +
            "AND e.statusEvento NOT IN ('Cancelado', 'Concluido') " +
            "AND ((e.dataHoraInicio < :fim) AND (e.dataHoraFim > :inicio))")
    List<Evento> findConflitantes(@Param("espacoId") Long espacoId,
                                  @Param("inicio") LocalDateTime inicio,
                                  @Param("fim") LocalDateTime fim);
}