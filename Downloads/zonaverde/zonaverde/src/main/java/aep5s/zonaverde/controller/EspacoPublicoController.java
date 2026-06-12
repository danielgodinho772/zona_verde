package aep5s.zonaverde.controller;

import aep5s.zonaverde.domain.entity.EspacoPublico;
import aep5s.zonaverde.domain.enums.TipoEspaco;
import aep5s.zonaverde.dto.EspacoPublicoDTO;
import aep5s.zonaverde.service.EspacoPublicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/espacos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EspacoPublicoController {
    private final EspacoPublicoService service;

    @GetMapping
    public List<EspacoPublico> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspacoPublico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/tipo/{tipo}")
    public List<EspacoPublico> listarPorTipo(@PathVariable TipoEspaco tipo) {
        return service.listarPorTipo(tipo);
    }

    @PostMapping
    public ResponseEntity<EspacoPublico> criar(@RequestBody EspacoPublicoDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping("/{id}/ocupacao")
    public ResponseEntity<String> ocupacao(
            @PathVariable Long id,
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(service.calcularOcupacao(id, inicio, fim));
    }
}
