package com.duoc.perfumessus.controller;

import com.duoc.perfumessus.dto.AuthRequest;
import com.duoc.perfumessus.dto.AuthResponse;
import com.duoc.perfumessus.model.Usuario;
import com.duoc.perfumessus.repository.UsuarioRepository;
import com.duoc.perfumessus.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        log.info("[AuthController] -> POST: Registrar usuario: {}", request.getEmail());

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getEmail());
        nuevo.setEmail(request.getEmail());
        nuevo.setClave(passwordEncoder.encode(request.getPassword()));
        nuevo.setRol("ROLE_USER");

        usuarioRepository.save(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("[AuthController] -> POST: Login para: {}", request.getEmail());

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String role = auth.getAuthorities().iterator().next().getAuthority();
        String token = jwtUtil.generateToken(request.getEmail(), role);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}