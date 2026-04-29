package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.negocio.dominio.EstadoPrestamoDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.UtilObjeto;

public final class EstadoPrestamoEntidadAssembler implements EntidadAssembler<EstadoPrestamoDominio, EstadoPrestamoEntidad> {

    private static EntidadAssembler<EstadoPrestamoDominio, EstadoPrestamoEntidad> INSTANCE;

    private EstadoPrestamoEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<EstadoPrestamoDominio, EstadoPrestamoEntidad> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new EstadoPrestamoEntidadAssembler();
        }
        return INSTANCE;
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
