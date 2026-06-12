package aep5s.zonaverde.domain.entity;

import aep5s.zonaverde.domain.state.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    @Column(name = "statusEvento")
    private String statusEvento = "Proposto";

    @ManyToOne
    @JoinColumn(name = "espacoPublico_id")
    @JsonManagedReference
    private EspacoPublico espacoPublico;

    @ManyToOne
    @JoinColumn(name = "proponente_id")
    private Usuario proponente;

    @Transient
    private EstadoEvento estado = new Proposto();

    public void avancarEstado() {
        this.estado = estado.proximoEstado();
        this.statusEvento = estado.getNome();
    }

    public void cancelar() {
        if (!estado.podeCancelar())
            throw new IllegalStateException("Este evento não pode ser cancelado.");
        this.estado = new Cancelado();
        this.statusEvento = "Cancelado";
    }

    @PostLoad
    private void restaurarEstado(){
        this.estado = switch (statusEvento){
            case "Em Analise" -> new EmAnalise();
            case "Aprovado" -> new Aprovado();
            case "Em Andamento" -> new EmAndamento();
            case "Concluido" -> new Concluido();
            case "Cancelado" -> new Cancelado();
            default -> new Proposto();
        };
    }
}
