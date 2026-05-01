package com.duoc.perfumessus.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.perfumessus.dto.PerfumeDTO;
import com.duoc.perfumessus.mapper.PerfumeMapper;
import com.duoc.perfumessus.model.Perfume;
import com.duoc.perfumessus.repository.PerfumeRepository;

@Service
public class PerfumeService {

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Autowired
    private PerfumeMapper perfumeMapper;

    public List<PerfumeDTO> obtenerTodos(){
        
        return perfumeRepository.findAll()
                .stream()
                .map(perfumeMapper::toDTO)
                .toList();
    }

    public PerfumeDTO obtenerPorId(Long id){

        return perfumeRepository.findById(id)
                .map(perfumeMapper::toDTO)
                .orElse(null);
    }

    public PerfumeDTO guardar(Perfume perfume){

        Perfume guardado = perfumeRepository.save(perfume);
        return perfumeMapper.toDTO(guardado);
    }

    public PerfumeDTO actualizar(Long id, Perfume detallesActualizado){

        Optional<Perfume> perfumeExistente = perfumeRepository.findById(id);

        if (perfumeExistente.isPresent()){

            Perfume perfumeActualizar = perfumeExistente.get();

            perfumeActualizar.setNombre(detallesActualizado.getNombre());
            perfumeActualizar.setMarca(detallesActualizado.getMarca());
            perfumeActualizar.setTipo(detallesActualizado.getTipo());
            perfumeActualizar.setMl(detallesActualizado.getMl());
            perfumeActualizar.setPrecio(detallesActualizado.getPrecio());

            if (detallesActualizado.getCategoria() != null){

                perfumeActualizar.setCategoria(detallesActualizado.getCategoria());
            }

            Perfume perfumeGuardado = perfumeRepository.save(perfumeActualizar);
            return perfumeMapper.toDTO(perfumeGuardado);
        }

        return null;
    }

    public boolean eliminar(Long id){

        if (perfumeRepository.existsById(id)){

            perfumeRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
