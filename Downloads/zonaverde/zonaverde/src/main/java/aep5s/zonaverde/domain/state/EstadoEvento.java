package aep5s.zonaverde.domain.state;

public interface EstadoEvento {
    EstadoEvento proximoEstado();
    boolean podeCancelar();
    String getNome();
}
