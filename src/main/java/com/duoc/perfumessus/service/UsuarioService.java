package com.duoc.perfumessus.service;

import com.duoc.perfumessus.dto.UsuarioDTO;
import com.duoc.perfumessus.mapper.UsuarioMapper;
import com.duoc.perfumessus.model.Usuario;
import com.duoc.perfumessus.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public List<UsuarioDTO> obtenerTodos() {
        log.info("[UsuarioService] -> Consultando todos los usuarios de la base de datos");
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    public UsuarioDTO obtenerPorId(Long id) {
        log.info("[UsuarioService] -> Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO)
                .orElse(null);
    }

    public UsuarioDTO guardar(Usuario usuario) {
        log.info("[UsuarioService] -> Guardando nuevo usuario con email: {}", usuario.getEmail());
        
        //Si no especifican rol, le damos el rol de usuario estándar
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("USER");
        }
        
        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }

    public UsuarioDTO actualizar(Long id, Usuario detallesActualizado) {
        log.info("[UsuarioService] -> Solicitud de actualización para usuario ID: {}", id);
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if (usuarioExistente.isPresent()) {
            Usuario u = usuarioExistente.get();
            u.setNombre(detallesActualizado.getNombre());
            u.setEmail(detallesActualizado.getEmail());
            
            if (detallesActualizado.getRol() != null && !detallesActualizado.getRol().isBlank()) {
                u.setRol(detallesActualizado.getRol());
            }

            // Si envían una clave nueva, la actualizamos.
            if (detallesActualizado.getClave() != null && !detallesActualizado.getClave().isBlank()) {
                u.setClave(detallesActualizado.getClave());
            }

            Usuario actualizado = usuarioRepository.save(u);
            log.info("[UsuarioService] -> Usuario ID: {} actualizado exitosamente", id);
            return usuarioMapper.toDTO(actualizado);
        }
        
        log.warn("[UsuarioService] -> Actualización fallida: Usuario ID {} no existe", id);
        return null;
    }

    public boolean eliminar(Long id) {
        log.info("[UsuarioService] -> Intentando eliminar usuario ID: {}", id);
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            log.info("[UsuarioService] -> Usuario ID: {} eliminado", id);
            return true;
        }
        log.warn("[UsuarioService] -> No se pudo eliminar: Usuario ID {} no encontrado", id);
        return false;
    }
}