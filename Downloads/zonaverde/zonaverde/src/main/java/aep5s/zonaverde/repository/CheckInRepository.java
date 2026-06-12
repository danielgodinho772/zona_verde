package aep5s.zonaverde.repository;

import aep5s.zonaverde.domain.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    @Query("SELECT COUNT(c) FROM CheckIn c WHERE c.espacoPublico.id = :espacoId " +
            "AND c.dataHora BETWEEN :inicio AND :fim")
    Long countByEspacoAndPeriodo(@Param("espacoId") Long espacoId, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
