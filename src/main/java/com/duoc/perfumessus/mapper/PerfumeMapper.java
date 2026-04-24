package com.duoc.perfumessus.mapper;

import org.springframework.stereotype.Component;

import com.duoc.perfumessus.dto.PerfumeDTO;
import com.duoc.perfumessus.model.Perfume;

/**
 * Componente encargado del mapeo y conversión de datos.
 * Su responsabilidad principal es traducir objetos de las clases
 * a objetos de transferencia de datos (DTOs) y viceversa. Centralizar esta lógica 
 * garantiza el principio de Responsabilidad única, manteniendo los 
 * Controladores y Servicios libres de código de transformación asi no ensuciamos el 
 * codigo con tanto codigo repetitivo (que es lo que hicimos en la primer prueba).
 */

@Component
public class PerfumeMapper {

    public PerfumeDTO toDTO(Perfume perfume){

        if (perfume == null){
            return null;
        }

        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(perfume.getId());
        dto.setNombre(perfume.getNombre());
        dto.setMarca(perfume.getMarca());
        dto.setTipo(perfume.getTipo());
        dto.setMl(perfume.getMl());
        dto.setPrecio(perfume.getPrecio());

        if (perfume.getCategoria() != null){

            dto.setNombreCategoria(perfume.getCategoria().getNombre());
        } else {

            dto.setNombreCategoria("Sin categoria asignada");
        }
        return dto;

    }

}
