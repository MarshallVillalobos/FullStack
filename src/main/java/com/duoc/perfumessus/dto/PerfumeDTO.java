package com.duoc.perfumessus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 * Objeto de Transferencia de Datos (DTO) para la entidad.
 * Este objeto se utiliza para encapsular y transportar los datos desde la 
 * capa de servicio hacia la capa de presentación. 
 * Actúa como una barrera de seguridad y diseño,
 * permitiendo exponer estrictamente la información necesaria para el cliente.
 */

public class PerfumeDTO {

    private Long id;
    private String nombre;
    private String marca;
    private String tipo;
    private Integer ml;
    private Double precio;
    private String nombreCategoria;

}
