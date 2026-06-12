package aep5s.zonaverde.domain.state;

public class EmAnalise implements EstadoEvento {
    @Override
    public EstadoEvento proximoEstado() {
        return new Aprovado();
    }

    @Override
    public boolean podeCancelar() {
        return true;
    }

    @Override
    public String getNome() {
        return "Em Analise";
    }
}
