package aep5s.zonaverde.domain.state;

public class EmAndamento implements EstadoEvento {
    @Override
    public EstadoEvento proximoEstado() {
        return new Concluido();
    }

    @Override
    public boolean podeCancelar() {
        return false;
    }

    @Override
    public String getNome() {
        return "Em Andamento";
    }
}
