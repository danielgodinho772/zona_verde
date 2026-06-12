package aep5s.zonaverde.domain.entity;

import aep5s.zonaverde.domain.enums.TipoEspaco;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "espacos_publicos")
public class EspacoPublico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String endereco;
    private TipoEspaco tipo;
    private Double latitude;
    private Double longitude;
    private Integer capacidadeEstimada;

    @OneToMany(mappedBy = "espacoPublico", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Evento> eventos;

    @OneToMany(mappedBy = "espacoPublico", cascade = CascadeType.ALL)
    private List<Reporte> reportes;

    @OneToMany(mappedBy = "espacoPublico", cascade = CascadeType.ALL)
    private List<CheckIn> checkIns;
}
