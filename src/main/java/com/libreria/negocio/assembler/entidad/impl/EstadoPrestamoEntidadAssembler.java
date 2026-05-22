package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.negocio.dominio.EstadoPrestamoDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class EstadoPrestamoEntidadAssembler implements EntidadAssembler<EstadoPrestamoDominio, EstadoPrestamoEntidad> {

    private static EntidadAssembler<EstadoPrestamoDominio, EstadoPrestamoEntidad> instancia;

    private EstadoPrestamoEntidadAssembler() {
        super();
    }

    public static final synchronized EntidadAssembler<EstadoPrestamoDominio, EstadoPrestamoEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new EstadoPrestamoEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public EstadoPrestamoDominio ensamblarDominio(final EstadoPrestamoEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new EstadoPrestamoEntidad.Builder().build());
        return new EstadoPrestamoDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public EstadoPrestamoEntidad ensamblarEntidad(final EstadoPrestamoDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new EstadoPrestamoDominio.Builder().build());
        return new EstadoPrestamoEntidad.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}