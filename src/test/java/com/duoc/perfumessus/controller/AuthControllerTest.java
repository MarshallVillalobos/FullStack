package com.duoc.perfumessus.controller;

import com.duoc.perfumessus.dto.AuthRequest;
import com.duoc.perfumessus.dto.AuthResponse;
import com.duoc.perfumessus.model.Usuario;
import com.duoc.perfumessus.repository.UsuarioRepository;
import com.duoc.perfumessus.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    // ─────────────────────────────────────────────
    // register
    // ─────────────────────────────────────────────

    @Test
    void register_retorna409_cuandoUsuarioYaExiste() {
        AuthRequest request = new AuthRequest();
        request.setEmail("marshall@test.com");
        request.setPassword("admin1234");

        when(usuarioRepository.existsByEmail("marshall@test.com")).thenReturn(true);

        var respuesta = authController.register(request);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("El usuario ya existe", respuesta.getBody());
    }

    @Test
    void register_retorna201_cuandoUsuarioNuevo() {
        AuthRequest request = new AuthRequest();
        request.setEmail("nuevo@test.com");
        request.setPassword("clave123");

        when(usuarioRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        when(passwordEncoder.encode("clave123")).thenReturn("$2a$clavecifrada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        var respuesta = authController.register(request);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals("Usuario registrado exitosamente", respuesta.getBody());
    }

    // ─────────────────────────────────────────────
    // login
    // ─────────────────────────────────────────────

    @Test
    void login_retorna200_conToken_cuandoCredencialesValidas() {
        AuthRequest request = new AuthRequest();
        request.setEmail("marshall@test.com");
        request.setPassword("admin1234");

        Authentication authMock = new UsernamePasswordAuthenticationToken(
                "marshall@test.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        when(authenticationManager.authenticate(any())).thenReturn(authMock);
        when(jwtUtil.generateToken("marshall@test.com", "ROLE_ADMIN")).thenReturn("token.jwt.generado");

        var respuesta = authController.login(request);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());

        AuthResponse body = respuesta.getBody();
        assertNotNull(body);
        assertEquals("token.jwt.generado", body.getToken());
    }

    @Test
    void login_lanzaExcepcion_cuandoCredencialesInvalidas() {
        AuthRequest request = new AuthRequest();
        request.setEmail("noexiste@test.com");
        request.setPassword("claveWrong");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciales incorrectas"));

        assertThrows(BadCredentialsException.class, () -> authController.login(request));
    }
}