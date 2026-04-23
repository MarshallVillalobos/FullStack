package com.duoc.perfumessus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PerfumeDTO {

    private Long id;
    private String nombre;
    private String marca;
    private String tipo;
    private Integer ml;
    private Double precio;
    private String nombreCategoria;

}
