package aep5s.zonaverde.controller;

import aep5s.zonaverde.domain.entity.Usuario;
import aep5s.zonaverde.dto.UsuarioDTO;
import aep5s.zonaverde.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
