package aep5s.zonaverde.service;

import aep5s.zonaverde.domain.entity.EspacoPublico;
import aep5s.zonaverde.domain.entity.Reporte;
import aep5s.zonaverde.domain.entity.Usuario;
import aep5s.zonaverde.dto.ReporteDTO;
import aep5s.zonaverde.repository.EspacoPublicoRepository;
import aep5s.zonaverde.repository.ReporteRepository;
import aep5s.zonaverde.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {
    private final ReporteRepository reporteRepository;
    private final EspacoPublicoRepository espacoRepository;
    private final UsuarioRepository usuarioRepository;

    public Reporte criar(ReporteDTO dto) {
        EspacoPublico espaco = espacoRepository.findById(dto.getEspacoPublicoId())
                .orElseThrow(() -> new RuntimeException("Espaço não encontrado"));
        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Reporte reporte = new Reporte();
        reporte.setDescricao(dto.getDescricao());
        reporte.setTipo(dto.getTipo());
        reporte.setDataAbertura(LocalDate.now());
        reporte.setConfirmacoes(0);
        reporte.setEspacoPublico(espaco);
        reporte.setAutor(autor);
        return reporteRepository.save(reporte);
    }

    public Reporte confirmar(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte não encontrado"));
        reporte.setConfirmacoes(reporte.getConfirmacoes() + 1);
        return reporteRepository.save(reporte);
    }

    public List<ReporteDTO> listarPorPrioridade() {
        return reporteRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Reporte::calcularPrioridade).reversed())
                .map(r -> {
                    ReporteDTO dto = new ReporteDTO();
                    dto.setId(r.getId());
                    dto.setDescricao(r.getDescricao());
                    dto.setTipo(r.getTipo());
                    dto.setDataAbertura(r.getDataAbertura());
                    dto.setConfirmacoes(r.getConfirmacoes());
                    dto.setEspacoPublicoId(r.getEspacoPublico().getId());
                    dto.setPrioridade(r.calcularPrioridade());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Reporte> listarPorEspaco(Long espacoId) {
        return reporteRepository.findByEspacoPublicoId(espacoId);
    }
}
