package com.duoc.perfumessus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "perfumes")
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "Debe especificar el tipo de perfume")
    private String tipo;

    @NotNull(message = "El tamaño en ml es obligatorio")
    @Min(value = 1, message = "Los milimetros deben ser mayor que 0")
    private Integer ml;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


}
