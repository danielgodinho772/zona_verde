package aep5s.zonaverde.dto;

import aep5s.zonaverde.domain.enums.TipoEspaco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EspacoPublicoDTO {
    private Long id;
    private String nome;
    private String endereco;
    private TipoEspaco tipo;
    private Double latitude;
    private Double longitude;
    private Integer capacidadeEstimada;
}
