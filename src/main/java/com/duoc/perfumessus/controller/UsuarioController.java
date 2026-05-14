package com.duoc.perfumessus.controller;

import com.duoc.perfumessus.dto.UsuarioDTO;
import com.duoc.perfumessus.model.Usuario;
import com.duoc.perfumessus.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        log.info("[UsuarioController] -> GET: Listar todos los usuarios");
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Long id) {
        log.info("[UsuarioController] -> GET: Buscar usuario ID {}", id);
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> guardar(@Valid @RequestBody Usuario usuario) {
        log.info("[UsuarioController] -> POST: Crear usuario");
        UsuarioDTO nuevoUsuario = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        log.info("[UsuarioController] -> PUT: Actualizar usuario ID {}", id);
        UsuarioDTO actualizado = usuarioService.actualizar(id, usuario);
        
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[UsuarioController] -> DELETE: Eliminar usuario ID {}", id);
        boolean eliminado = usuarioService.eliminar(id);
        
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}