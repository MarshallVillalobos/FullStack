package com.duoc.perfumessus.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.perfumessus.dto.PerfumeDTO;
import com.duoc.perfumessus.mapper.PerfumeMapper;
import com.duoc.perfumessus.model.Perfume;
import com.duoc.perfumessus.repository.PerfumeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PerfumeService {

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Autowired
    private PerfumeMapper perfumeMapper;

    public List<PerfumeDTO> obtenerTodos(){
        log.info("[PerfumeService] -> Consultando la lista completa de perfumes en la base de datos");
        
        return perfumeRepository.findAll()
                .stream()
                .map(perfumeMapper::toDTO)
                .toList();
    }

    public PerfumeDTO obtenerPorId(Long id){
        log.info("[PerfumeService] -> Ejecutando búsqueda de perfume con ID: {}", id);
        
        PerfumeDTO dto = perfumeRepository.findById(id)
                .map(perfumeMapper::toDTO)
                .orElse(null);
                
        // Evaluamos si el resultado fue nulo para lanzar una advertencia en la terminal
        if (dto == null) {
            log.warn("[PerfumeService] -> Búsqueda fallida: El perfume con ID: {} no existe", id);
        } else {
            log.info("[PerfumeService] -> Perfume con ID: {} encontrado exitosamente", id);
        }
        
        return dto;
    }

    public PerfumeDTO guardar(Perfume perfume){
        log.info("[PerfumeService] -> Iniciando guardado de nuevo perfume: {}", perfume.getNombre());
        
        Perfume guardado = perfumeRepository.save(perfume);
        
        log.info("[PerfumeService] -> Perfume guardado correctamente en BD con el nuevo ID: {}", guardado.getId());
        return perfumeMapper.toDTO(guardado);
    }

    public PerfumeDTO actualizar(Long id, Perfume detallesActualizado){
        log.info("[PerfumeService] -> Solicitud de actualización para el perfume ID: {}", id);
        
        Optional<Perfume> perfumeExistente = perfumeRepository.findById(id);

        if (perfumeExistente.isPresent()){
            log.info("[PerfumeService] -> Perfume ID: {} encontrado, aplicando nuevos datos...", id);
            
            Perfume perfumeActualizar = perfumeExistente.get();

            perfumeActualizar.setNombre(detallesActualizado.getNombre());
            perfumeActualizar.setMarca(detallesActualizado.getMarca());
            perfumeActualizar.setTipo(detallesActualizado.getTipo());
            perfumeActualizar.setMl(detallesActualizado.getMl());
            perfumeActualizar.setPrecio(detallesActualizado.getPrecio());

            if (detallesActualizado.getCategoria() != null){
                log.info("[PerfumeService] -> Modificando la categoría asociada al perfume ID: {}", id);
                perfumeActualizar.setCategoria(detallesActualizado.getCategoria());
            }

            Perfume perfumeGuardado = perfumeRepository.save(perfumeActualizar);
            log.info("[PerfumeService] -> Perfume ID: {} actualizado y guardado con éxito", id);
            
            return perfumeMapper.toDTO(perfumeGuardado);
        }

        // Si se manda ID falso para botar el método PUT, queda registrado aquí
        log.warn("[PerfumeService] -> Actualización abortada: No se encontró un perfume con el ID: {}", id);
        return null;
    }

    public boolean eliminar(Long id){
        log.info("[PerfumeService] -> Solicitud de eliminación física para el perfume ID: {}", id);
        
        if (perfumeRepository.existsById(id)){
            
            perfumeRepository.deleteById(id);
            log.info("[PerfumeService] -> Perfume ID: {} eliminado permanentemente de la base de datos", id);
            return true;
        }

        // Si se intenta borrar un perfume que ya no existe (o nunca existió)
        log.warn("[PerfumeService] -> Eliminación abortada: El perfume con ID: {} no existe", id);
        return false;
    }
}
