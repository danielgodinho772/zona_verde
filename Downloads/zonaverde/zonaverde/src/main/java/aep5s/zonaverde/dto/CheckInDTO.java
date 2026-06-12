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
public class CheckInDTO {
    private Long id;
    private Long usuarioId;
    private Long espacoPublicoId;
    private LocalDateTime dataHora;
}
