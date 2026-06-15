package com.duoc.perfumessus.controller;

import com.duoc.perfumessus.dto.PerfumeDTO;
import com.duoc.perfumessus.model.Perfume;
import com.duoc.perfumessus.service.FragellaService;
import com.duoc.perfumessus.service.PerfumeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfumeControllerTest {

    @Mock
    private PerfumeService perfumeService;

    @Mock
    private FragellaService fragellaService;

    @InjectMocks
    private PerfumeController perfumeController;

    @Test
    void crearPerfume_retorna201_cuandoDatosValidos() {

        // Preparamos el objeto perfume que el cliente enviaría
        Perfume perfume = new Perfume();
        perfume.setNombre("Sauvage");
        perfume.setMarca("Dior");
        perfume.setTipo("Eau de Parfum");
        perfume.setMl(60);
        perfume.setPrecio(115990.0);
        perfume.setStock(25);

        // Preparamos el DTO que el servicio devolvería al guardar
        PerfumeDTO perfumeDTO = new PerfumeDTO();
        perfumeDTO.setId(1L);
        perfumeDTO.setNombre("Sauvage");
        perfumeDTO.setMarca("Dior");
        perfumeDTO.setTipo("Eau de Parfum");
        perfumeDTO.setMl(60);
        perfumeDTO.setPrecio(115990.0);
        perfumeDTO.setNombreCategoria("Sin categoria asignada");

        // Simulamos el comportamiento del servicio (sin tocar la BD)
        when(perfumeService.guardar(perfume)).thenReturn(perfumeDTO);

        // Llamamos al método del controlador que queremos probar
        var respuesta = perfumeController.guardar(perfume);

        // Verificamos que la respuesta no sea nula
        assertNotNull(respuesta);

        // Verificamos que el código HTTP sea 201 Created
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

        // Verificamos que el cuerpo exista
        var body = respuesta.getBody();
        assertNotNull(body);

        // Verificamos un dato clave del cuerpo
        assertEquals("Sauvage", body.getNombre());
        assertEquals("Dior", body.getMarca());
    }
}