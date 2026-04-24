package com.duoc.perfumessus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.perfumessus.model.Perfume;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long>{
    /** 
    *Con JPaRepository tenemos el crud basico
    *findAll() (Buscar todos).
    *findById(Long id) (Buscar por ID).
    *save(Perfume perfume) (Guardar o actualizar).
    * deleteById(Long id) (Borrar por ID).
    * Ya si requerimos de consultas más especificas agregamos el metodo más abajo 
    */
}
