package com.duoc.perfumessus.service;

import com.duoc.perfumessus.dto.PerfumeDTO;
import com.duoc.perfumessus.mapper.PerfumeMapper;
import com.duoc.perfumessus.model.Perfume;
import com.duoc.perfumessus.repository.PerfumeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfumeServiceTest {

    @Mock
    private PerfumeRepository perfumeRepository;

    @Mock
    private PerfumeMapper perfumeMapper;

    @InjectMocks
    private PerfumeService perfumeService;

    // ─────────────────────────────────────────────
    // obtenerPorId
    // ─────────────────────────────────────────────

    @Test
    void obtenerPorId_retornaDTO_cuandoPerfumeExiste() {
        Perfume perfume = new Perfume();
        perfume.setId(1L);
        perfume.setNombre("Sauvage");
        perfume.setMarca("Dior");
        perfume.setTipo("Eau de Parfum");
        perfume.setMl(60);
        perfume.setPrecio(115990.0);
        perfume.setStock(25);

        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(1L);
        dto.setNombre("Sauvage");
        dto.setMarca("Dior");

        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
        when(perfumeMapper.toDTO(perfume)).thenReturn(dto);

        PerfumeDTO resultado = perfumeService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("Sauvage", resultado.getNombre());
        assertEquals("Dior", resultado.getMarca());
    }

    @Test
    void obtenerPorId_retornaNull_cuandoPerfumeNoExiste() {
        when(perfumeRepository.findById(999L)).thenReturn(Optional.empty());

        PerfumeDTO resultado = perfumeService.obtenerPorId(999L);

        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // guardar
    // ─────────────────────────────────────────────

    @Test
    void guardar_retornaDTO_cuandoPerfumeValido() {
        Perfume perfume = new Perfume();
        perfume.setNombre("Bleu de Chanel");
        perfume.setMarca("Chanel");
        perfume.setTipo("Eau de Parfum");
        perfume.setMl(100);
        perfume.setPrecio(189990.0);
        perfume.setStock(10);

        Perfume guardado = new Perfume();
        guardado.setId(2L);
        guardado.setNombre("Bleu de Chanel");
        guardado.setMarca("Chanel");

        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(2L);
        dto.setNombre("Bleu de Chanel");
        dto.setMarca("Chanel");

        when(perfumeRepository.save(perfume)).thenReturn(guardado);
        when(perfumeMapper.toDTO(guardado)).thenReturn(dto);

        PerfumeDTO resultado = perfumeService.guardar(perfume);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("Bleu de Chanel", resultado.getNombre());
    }

    // ─────────────────────────────────────────────
    // actualizar
    // ─────────────────────────────────────────────

    @Test
    void actualizar_retornaDTO_cuandoPerfumeExiste() {
        Perfume existente = new Perfume();
        existente.setId(1L);
        existente.setNombre("Sauvage");
        existente.setMarca("Dior");
        existente.setTipo("Eau de Parfum");
        existente.setMl(60);
        existente.setPrecio(115990.0);
        existente.setStock(25);

        Perfume actualizado = new Perfume();
        actualizado.setNombre("Sauvage Elixir");
        actualizado.setMarca("Dior");
        actualizado.setTipo("Extrait de Parfum");
        actualizado.setMl(60);
        actualizado.setPrecio(155000.0);
        actualizado.setStock(10);

        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(1L);
        dto.setNombre("Sauvage Elixir");
        dto.setMarca("Dior");

        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(perfumeRepository.save(existente)).thenReturn(existente);
        when(perfumeMapper.toDTO(existente)).thenReturn(dto);

        PerfumeDTO resultado = perfumeService.actualizar(1L, actualizado);

        assertNotNull(resultado);
        assertEquals("Sauvage Elixir", resultado.getNombre());
    }

    @Test
    void actualizar_retornaNull_cuandoPerfumeNoExiste() {
        Perfume actualizado = new Perfume();
        actualizado.setNombre("Fantasma");
        actualizado.setMarca("Nadie");
        actualizado.setTipo("Tipo");
        actualizado.setMl(50);
        actualizado.setPrecio(50000.0);
        actualizado.setStock(1);

        when(perfumeRepository.findById(999L)).thenReturn(Optional.empty());

        PerfumeDTO resultado = perfumeService.actualizar(999L, actualizado);

        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // eliminar
    // ─────────────────────────────────────────────

    @Test
    void eliminar_retornaTrue_cuandoPerfumeExiste() {
        when(perfumeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(perfumeRepository).deleteById(1L);

        boolean resultado = perfumeService.eliminar(1L);

        assertTrue(resultado);
        verify(perfumeRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_retornaFalse_cuandoPerfumeNoExiste() {
        when(perfumeRepository.existsById(999L)).thenReturn(false);

        boolean resultado = perfumeService.eliminar(999L);

        assertFalse(resultado);
        verify(perfumeRepository, never()).deleteById(any());
    }
}