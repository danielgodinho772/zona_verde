package aep5s.zonaverde.domain.state;

public class Concluido implements EstadoEvento {
    @Override
    public EstadoEvento proximoEstado() {
        throw new IllegalStateException("Evento concluído não pode avançar de estado.");
    }

    @Override
    public boolean podeCancelar() {
        return false;
    }

    @Override
    public String getNome() {
        return "Concluido";
    }
}
