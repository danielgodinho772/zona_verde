package aep5s.zonaverde.dto;

import aep5s.zonaverde.domain.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
}
