package com.duoc.perfumessus.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Clave de prueba (mínimo 32 caracteres para HMAC-SHA)
    private static final String SECRET = "perfumessus-clave-secreta-jwt-2026-cambiar-en-produccion";

    @BeforeEach
    void setUp() {
        // ¡Mira qué fácil es ahora gracias al cambio del profe!
        // Solo instanciamos la clase pasándole el texto secreto, sin Reflection ni init()
        jwtUtil = new JwtUtil(SECRET);
    }

    // ─────────────────────────────────────────────
    // generateToken / extractUsername / extractRole
    // ─────────────────────────────────────────────

    @Test
    void generateToken_retornaTokenNoNulo() {
        String token = jwtUtil.generateToken("marshall@test.com", "ROLE_ADMIN");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_retornaEmailCorrecto() {
        String token = jwtUtil.generateToken("marshall@test.com", "ROLE_ADMIN");
        String username = jwtUtil.extractUsername(token);
        assertEquals("marshall@test.com", username);
    }

    @Test
    void extractRole_retornaRolCorrecto() {
        String token = jwtUtil.generateToken("marshall@test.com", "ROLE_ADMIN");
        String role = jwtUtil.extractRole(token);
        assertEquals("ROLE_ADMIN", role);
    }

    @Test
    void extractRole_retornaRoleUser_cuandoEsUserNormal() {
        String token = jwtUtil.generateToken("user@test.com", "ROLE_USER");
        String role = jwtUtil.extractRole(token);
        assertEquals("ROLE_USER", role);
    }

    // ─────────────────────────────────────────────
    // validateToken
    // ─────────────────────────────────────────────

    @Test
    void validateToken_retornaTrue_cuandoTokenValido() {
        String token = jwtUtil.generateToken("marshall@test.com", "ROLE_ADMIN");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_retornaFalse_cuandoTokenInvalido() {
        String tokenFalso = "esto.no.es.un.token.valido";
        assertFalse(jwtUtil.validateToken(tokenFalso));
    }

    @Test
    void validateToken_retornaFalse_cuandoTokenVacio() {
        assertFalse(jwtUtil.validateToken(""));
    }
}