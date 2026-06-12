package aep5s.zonaverde.dto;

import aep5s.zonaverde.domain.enums.TipoReporte;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTO {
    private Long id;
    private String descricao;
    private TipoReporte tipo;
    private LocalDate dataAbertura;
    private Integer confirmacoes;
    private Long espacoPublicoId;
    private Long autorId;
    private Integer prioridade;
}
