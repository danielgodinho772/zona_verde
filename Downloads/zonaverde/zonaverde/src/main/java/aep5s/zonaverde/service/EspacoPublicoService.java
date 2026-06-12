package aep5s.zonaverde.service;

import aep5s.zonaverde.domain.entity.EspacoPublico;
import aep5s.zonaverde.domain.enums.TipoEspaco;
import aep5s.zonaverde.dto.EspacoPublicoDTO;
import aep5s.zonaverde.repository.CheckInRepository;
import aep5s.zonaverde.repository.EspacoPublicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EspacoPublicoService {

    private final EspacoPublicoRepository espacoRepository;
    private final CheckInRepository checkInRepository;

    public List<EspacoPublico> listarTodos() {
        return espacoRepository.findAll();
    }

    public List<EspacoPublico> listarPorTipo(TipoEspaco tipo) {
        return espacoRepository.findByTipo(tipo);
    }

    public EspacoPublico buscarPorId(Long id) {
        return espacoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado: " + id));
    }

    public EspacoPublico salvar(EspacoPublicoDTO dto) {
        EspacoPublico espaco = new EspacoPublico();
        espaco.setNome(dto.getNome());
        espaco.setEndereco(dto.getEndereco());
        espaco.setTipo(dto.getTipo());
        espaco.setLatitude(dto.getLatitude());
        espaco.setLongitude(dto.getLongitude());
        espaco.setCapacidadeEstimada(dto.getCapacidadeEstimada());
        return espacoRepository.save(espaco);
    }

    public Long contarCheckIns(Long espacoId, LocalDateTime inicio, LocalDateTime fim) {
        return checkInRepository.countByEspacoAndPeriodo(espacoId, inicio, fim);
    }

    public String calcularOcupacao(Long espacoId, LocalDateTime inicio, LocalDateTime fim) {
        EspacoPublico espaco = buscarPorId(espacoId);
        Long checkins = contarCheckIns(espacoId, inicio, fim);
        int capacidade = espaco.getCapacidadeEstimada();
        double percentual = (checkins * 100.0) / capacidade;
        if (percentual < 40) return "Vazio";
        if (percentual < 75) return "Moderado";
        return "Lotado";
    }
}