package aep5s.zonaverde.service;

import aep5s.zonaverde.domain.entity.CheckIn;
import aep5s.zonaverde.domain.entity.EspacoPublico;
import aep5s.zonaverde.domain.entity.Usuario;
import aep5s.zonaverde.dto.CheckInDTO;
import aep5s.zonaverde.repository.CheckInRepository;
import aep5s.zonaverde.repository.EspacoPublicoRepository;
import aep5s.zonaverde.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;
    private final EspacoPublicoRepository espacoRepository;
    private final UsuarioRepository usuarioRepository;

    public CheckIn realizar(CheckInDTO dto) {
        EspacoPublico espaco = espacoRepository.findById(dto.getEspacoPublicoId())
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado"));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CheckIn checkIn = new CheckIn();
        checkIn.setEspacoPublico(espaco);
        checkIn.setUsuario(usuario);
        checkIn.setDataHora(LocalDateTime.now());
        return checkInRepository.save(checkIn);
    }
}
