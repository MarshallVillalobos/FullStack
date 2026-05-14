package com.duoc.perfumessus.controller;

import com.duoc.perfumessus.dto.FragellaDTO;
import com.duoc.perfumessus.dto.PerfumeDTO;
import com.duoc.perfumessus.model.Perfume;
import com.duoc.perfumessus.service.FragellaService;
import com.duoc.perfumessus.service.PerfumeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/perfumes")
@Slf4j
public class PerfumeController {

    @Autowired
    private FragellaService fragellaService;

    @Autowired
    private PerfumeService perfumeService;

    @GetMapping
    public ResponseEntity<List<PerfumeDTO>> obtenerTodos() {
        log.info("[PerfumeController] -> Solicitud GET: Listar todos los perfumes.");
        List<PerfumeDTO> perfumes = perfumeService.obtenerTodos();
        
        if (perfumes.isEmpty()) {
            log.info("[PerfumeController] -> No se encontraron perfumes en el catálogo.");
            return ResponseEntity.noContent().build();
        }
        
        log.info("[PerfumeController] -> Retornando {} perfumes.", perfumes.size());
        return ResponseEntity.ok(perfumes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfumeDTO> obtenerPorId(@PathVariable Long id) {
        log.info("[PerfumeController] -> Solicitud GET: Buscar perfume ID: {}", id);
        PerfumeDTO perfume = perfumeService.obtenerPorId(id);
        
        if (perfume != null) {
            return ResponseEntity.ok(perfume);
        }
        
        log.warn("[PerfumeController] -> El perfume con ID: {} no fue localizado.", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PerfumeDTO> guardar(@Valid @RequestBody Perfume perfume) {
        log.info("[PerfumeController] -> Solicitud POST: Creando nuevo perfume: {}", perfume.getNombre());
        PerfumeDTO perfumeGuardado = perfumeService.guardar(perfume);
        
        log.info("[PerfumeController] -> Registro creado con éxito. Asignado ID: {}", perfumeGuardado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(perfumeGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfumeDTO> actualizar(@PathVariable Long id, @Valid @RequestBody Perfume perfume) {
        log.info("[PerfumeController] -> Solicitud PUT: Actualizar perfume ID: {}", id);
        PerfumeDTO perfumeActualizado = perfumeService.actualizar(id, perfume);
        
        if (perfumeActualizado != null) {
            log.info("[PerfumeController] -> Perfume ID: {} actualizado correctamente.", id);
            return ResponseEntity.ok(perfumeActualizado);
        }
        
        log.warn("[PerfumeController] -> Falló la actualización: ID {} no existe.", id);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[PerfumeController] -> Solicitud DELETE: Eliminar perfume ID: {}", id);
        boolean eliminado = perfumeService.eliminar(id);
        
        if (eliminado) {
            log.info("[PerfumeController] -> Perfume ID: {} eliminado exitosamente.", id);
            return ResponseEntity.noContent().build();
        }
        
        log.warn("[PerfumeController] -> No se pudo eliminar: ID {} no encontrado.", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/externo")
    public ResponseEntity<List<FragellaDTO>> buscarEnApiExterna(@RequestParam String nombre) {
        log.info("[PerfumeController] -> Solicitud GET: Buscar en Fragella: {}", nombre);
        
        List<FragellaDTO> resultados = fragellaService.buscarPerfumeExterno(nombre);
        
        if (resultados != null && !resultados.isEmpty()) {
            return ResponseEntity.ok(resultados);
        }
        
        return ResponseEntity.noContent().build();
    }
}