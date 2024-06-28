package com.jose.microservicio_productos_crud_reactivo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(value = "productos")
public class Producto implements Persistable<Integer>{
    @Column("codProducto")
    @Id
    private int codigo;
    private String nombre;
    private String categoria;
    @Column("precioUnitario")
    private double precioUnitario;
    private int stock;

    @Transient
    private boolean esNuevo;

    @Override
    @Nullable
    public Integer getId() {
        return codigo;
    }

    @Override
    public boolean isNew() {
        return esNuevo;
    }
}
