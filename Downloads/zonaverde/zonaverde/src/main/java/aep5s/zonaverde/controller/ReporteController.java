package aep5s.zonaverde.controller;

import aep5s.zonaverde.domain.entity.Reporte;
import aep5s.zonaverde.dto.ReporteDTO;
import aep5s.zonaverde.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReporteController {
    private final ReporteService service;

    @PostMapping
    public ResponseEntity<Reporte> criar(@RequestBody ReporteDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Reporte> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @GetMapping("/prioridade")
    public List<ReporteDTO> listarPorPrioridade() {
        return service.listarPorPrioridade();
    }

    @GetMapping("/espaco/{espacoId}")
    public List<Reporte> listarPorEspaco(@PathVariable Long espacoId) {
        return service.listarPorEspaco(espacoId);
    }
}
