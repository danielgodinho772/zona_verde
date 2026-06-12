package aep5s.zonaverde.domain.state;

public class Cancelado implements EstadoEvento {
    @Override
    public EstadoEvento proximoEstado() {
        throw new IllegalStateException("Evento cancelado não pode avançar de estado.");
    }
    @Override
    public boolean podeCancelar() {
        return false;
    }
    @Override
    public String getNome() {
        return "Cancelado";
    }
}
