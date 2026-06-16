package com.duoc.perfumessus.service;

import com.duoc.perfumessus.dto.UsuarioDTO;
import com.duoc.perfumessus.mapper.UsuarioMapper;
import com.duoc.perfumessus.model.Usuario;
import com.duoc.perfumessus.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    // ─────────────────────────────────────────────
    // obtenerPorId
    // ─────────────────────────────────────────────

    @Test
    void obtenerPorId_retornaDTO_cuandoUsuarioExiste() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Marshall Villalobos");
        usuario.setEmail("marshall@test.com");
        usuario.setClave("clave123");
        usuario.setRol("ROLE_ADMIN");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNombre("Marshall Villalobos");
        dto.setEmail("marshall@test.com");
        dto.setRol("ROLE_ADMIN");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(dto);

        UsuarioDTO resultado = usuarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("Marshall Villalobos", resultado.getNombre());
        assertEquals("marshall@test.com", resultado.getEmail());
    }

    @Test
    void obtenerPorId_retornaNull_cuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        UsuarioDTO resultado = usuarioService.obtenerPorId(999L);

        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // guardar
    // ─────────────────────────────────────────────

    @Test
    void guardar_asignaRolUser_cuandoNoSeespecificaRol() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Sin Rol");
        usuario.setEmail("sinrol@test.com");
        usuario.setClave("clave123");
        // rol null intencionalmente

        Usuario guardado = new Usuario();
        guardado.setId(1L);
        guardado.setNombre("Sin Rol");
        guardado.setEmail("sinrol@test.com");
        guardado.setRol("ROLE_USER");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNombre("Sin Rol");
        dto.setEmail("sinrol@test.com");
        dto.setRol("ROLE_USER");

        when(usuarioRepository.save(usuario)).thenReturn(guardado);
        when(usuarioMapper.toDTO(guardado)).thenReturn(dto);

        UsuarioDTO resultado = usuarioService.guardar(usuario);

        assertNotNull(resultado);
        assertEquals("ROLE_USER", resultado.getRol());
    }

    // ─────────────────────────────────────────────
    // actualizar
    // ─────────────────────────────────────────────

    @Test
    void actualizar_retornaDTO_cuandoUsuarioExiste() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombre("Marshall");
        existente.setEmail("marshall@test.com");
        existente.setClave("claveVieja");
        existente.setRol("ROLE_USER");

        Usuario detalles = new Usuario();
        detalles.setNombre("Marshall Actualizado");
        detalles.setEmail("marshall@test.com");
        detalles.setClave("claveNueva");
        detalles.setRol("ROLE_ADMIN");

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNombre("Marshall Actualizado");
        dto.setEmail("marshall@test.com");
        dto.setRol("ROLE_ADMIN");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);
        when(usuarioMapper.toDTO(existente)).thenReturn(dto);

        UsuarioDTO resultado = usuarioService.actualizar(1L, detalles);

        assertNotNull(resultado);
        assertEquals("Marshall Actualizado", resultado.getNombre());
        assertEquals("ROLE_ADMIN", resultado.getRol());
    }

    @Test
    void actualizar_retornaNull_cuandoUsuarioNoExiste() {
        Usuario detalles = new Usuario();
        detalles.setNombre("Fantasma");
        detalles.setEmail("fantasma@test.com");
        detalles.setClave("clave");

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        UsuarioDTO resultado = usuarioService.actualizar(999L, detalles);

        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // eliminar
    // ─────────────────────────────────────────────

    @Test
    void eliminar_retornaTrue_cuandoUsuarioExiste() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        boolean resultado = usuarioService.eliminar(1L);

        assertTrue(resultado);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_retornaFalse_cuandoUsuarioNoExiste() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);

        boolean resultado = usuarioService.eliminar(999L);

        assertFalse(resultado);
        verify(usuarioRepository, never()).deleteById(any());
    }
}