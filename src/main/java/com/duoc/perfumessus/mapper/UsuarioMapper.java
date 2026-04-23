package com.duoc.perfumessus.mapper;

import org.springframework.stereotype.Component;

import com.duoc.perfumessus.dto.UsuarioDTO;
import com.duoc.perfumessus.model.Usuario;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario){

        if (usuario == null){
            return null;
        }

        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getRol()
        );
    }

}
