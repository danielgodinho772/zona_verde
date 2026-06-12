package aep5s.zonaverde.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String statusEvento;
    private Long espacoPublicoId;
    private Long proponenteId;
}
