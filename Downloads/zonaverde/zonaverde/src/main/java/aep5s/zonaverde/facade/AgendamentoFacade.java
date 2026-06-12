package aep5s.zonaverde.facade;

import aep5s.zonaverde.domain.entity.Evento;
import aep5s.zonaverde.domain.entity.EspacoPublico;
import aep5s.zonaverde.domain.entity.Usuario;
import aep5s.zonaverde.dto.EventoDTO;
import aep5s.zonaverde.repository.EventoRepository;
import aep5s.zonaverde.repository.EspacoPublicoRepository;
import aep5s.zonaverde.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AgendamentoFacade {

    private final EventoRepository eventoRepo;
    private final EspacoPublicoRepository espacoRepo;
    private final UsuarioRepository usuarioRepo;

    public Evento proporEvento(EventoDTO dto) {
        verificarConflito(dto);
        validarProponente(dto.getProponenteId());
        Evento evento = criarEvento(dto);
        return eventoRepo.save(evento);
    }

    public Evento avancarEstado(Long eventoId) {
        Evento evento = buscarEvento(eventoId);
        evento.avancarEstado();
        return eventoRepo.save(evento);
    }

    public Evento cancelar(Long eventoId) {
        Evento evento = buscarEvento(eventoId);
        evento.cancelar();
        return eventoRepo.save(evento);
    }

    public List<Evento> listarPorEspaco(Long espacoId) {
        return eventoRepo.findByEspacoPublicoId(espacoId);
    }

    public List<Evento> listarTodos() {
        return eventoRepo.findAll();
    }

    private void verificarConflito(EventoDTO dto) {
        List<Evento> conflitos = eventoRepo.findConflitantes(
                dto.getEspacoPublicoId(),
                dto.getDataHoraInicio(),
                dto.getDataHoraFim()
        );
        if (!conflitos.isEmpty()) {
            throw new IllegalStateException(
                    "Conflito de horário: já existe um evento neste espaço neste período."
            );
        }
    }

    private void validarProponente(Long proponenteId) {
        usuarioRepo.findById(proponenteId)
                .orElseThrow(() -> new RuntimeException("Proponente não encontrado: " + proponenteId));
    }

    private Evento criarEvento(EventoDTO dto) {
        EspacoPublico espaco = espacoRepo.findById(dto.getEspacoPublicoId())
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado: " + dto.getEspacoPublicoId()));
        Usuario proponente = usuarioRepo.findById(dto.getProponenteId())
                .orElseThrow(() -> new RuntimeException("Proponente não encontrado: " + dto.getProponenteId()));

        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setDescricao(dto.getDescricao());
        evento.setDataHoraInicio(dto.getDataHoraInicio());
        evento.setDataHoraFim(dto.getDataHoraFim());
        evento.setEspacoPublico(espaco);
        evento.setProponente(proponente);
        evento.setStatusEvento("Proposto");
        return evento;
    }

    private Evento buscarEvento(Long id) {
        return eventoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado: " + id));
    }
}