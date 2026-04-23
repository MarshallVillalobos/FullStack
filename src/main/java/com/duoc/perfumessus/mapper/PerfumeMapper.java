package com.duoc.perfumessus.mapper;

import org.springframework.stereotype.Component;

import com.duoc.perfumessus.dto.PerfumeDTO;
import com.duoc.perfumessus.model.Perfume;

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
