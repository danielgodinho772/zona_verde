package aep5s.zonaverde.controller;

import aep5s.zonaverde.domain.entity.Evento;
import aep5s.zonaverde.dto.EventoDTO;
import aep5s.zonaverde.facade.AgendamentoFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventoController {
    private final AgendamentoFacade facade;

    @GetMapping
    public List<Evento> listarTodos() {
        return facade.listarTodos();
    }

    @GetMapping("/espaco/{espacoId}")
    public List<Evento> listarPorEspaco(@PathVariable Long espacoId) {
        return facade.listarPorEspaco(espacoId);
    }

    @PostMapping
    public ResponseEntity<Evento> propor(@RequestBody EventoDTO dto) {
        return ResponseEntity.ok(facade.proporEvento(dto));
    }

    @PatchMapping("/{id}/avancar")
    public ResponseEntity<Evento> avancar(@PathVariable Long id) {
        return ResponseEntity.ok(facade.avancarEstado(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Evento> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(facade.cancelar(id));
    }
}
