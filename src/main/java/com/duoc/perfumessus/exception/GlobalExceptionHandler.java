package com.duoc.perfumessus.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. ESCUDO DE VALIDACIÓN (Detallado con JSON)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // 2. ESCUDO DE BASE DE DATOS (Protege contra relaciones rotas o datos duplicados)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> manejarErroresDeBaseDeDatos(DataIntegrityViolationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Conflicto de integridad en la base de datos.");
        error.put("detalle", "Es posible que esté intentando ingresar un dato que ya existe (ej. email duplicado) o utilizando una relación incorrecta.");
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 3. ESCUDO CATASTRÓFICO (El último recurso si ocurre algo inesperado)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErroresInesperados(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Ocurrió un error interno en el servidor.");
        error.put("detalle", ex.getMessage()); // Opcional: muestra el mensaje técnico
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, String>> manejarErroresWebClient(WebClientResponseException ex) {
        Map<String, String> error = new LinkedHashMap<>();

        // Caso 1: La API externa dice que no encontró nada
        if (ex.getStatusCode().value() == 404) {
            error.put("error", "Perfume no encontrado en el catálogo externo.");
            error.put("detalle", "Fragella no devolvió resultados para esta búsqueda.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // Caso 2: Problemas con nuestra API Key - 401 No Autorizado o 403 Prohibido
        if (ex.getStatusCode().value() == 401 || ex.getStatusCode().value() == 403) {
            error.put("error", "Acceso denegado a la API externa.");
            error.put("detalle", "Verifique que la API Key de Fragella esté configurada correctamente y no haya expirado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        // Caso 3: Cualquier otro fallo (la API externa se cayó, demoró mucho, etc.) 502 Bad Gateway
        error.put("error", "Error de comunicación con el proveedor externo.");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }
}
