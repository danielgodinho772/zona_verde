package aep5s.zonaverde.service;

import aep5s.zonaverde.domain.entity.Cidadao;
import aep5s.zonaverde.domain.entity.Gestor;
import aep5s.zonaverde.domain.entity.Usuario;
import aep5s.zonaverde.domain.enums.TipoUsuario;
import aep5s.zonaverde.dto.UsuarioDTO;
import aep5s.zonaverde.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public Usuario salvar(UsuarioDTO dto) {
        Usuario usuario = dto.getTipo() == TipoUsuario.GESTOR ? new Gestor() : new Cidadao();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTipo(dto.getTipo());
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
    }
}
