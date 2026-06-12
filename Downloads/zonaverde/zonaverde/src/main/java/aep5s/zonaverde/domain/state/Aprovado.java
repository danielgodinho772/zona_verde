package aep5s.zonaverde.domain.state;

public class Aprovado implements EstadoEvento{
    @Override
    public EstadoEvento proximoEstado(){
        return new EmAndamento();
    }

    @Override
    public boolean podeCancelar(){
        return true;
    }

    @Override
    public String getNome() {
        return "Aprovado";
    }
}
