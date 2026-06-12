package aep5s.zonaverde.domain.entity;

import aep5s.zonaverde.domain.enums.TipoReporte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reportes")
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private TipoReporte tipo;
    private LocalDate dataAbertura;
    private Integer confirmacoes = 0;

    @ManyToOne
    @JoinColumn(name = "espacoPublico_id")
    private EspacoPublico espacoPublico;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    public int calcularPrioridade(){
        long diasEmAberto = java.time.temporal.ChronoUnit.DAYS.between(dataAbertura, LocalDate.now());
        return (confirmacoes * 2) + (int) diasEmAberto;
    }
}
