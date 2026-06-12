package aep5s.zonaverde.domain.state;

public class Proposto implements EstadoEvento {
    @Override
    public EstadoEvento proximoEstado() {
        return new EmAnalise();
    }

    @Override
    public boolean podeCancelar() {
        return true;
    }

    @Override
    public String getNome() {
        return "Proposto";
    }
}
