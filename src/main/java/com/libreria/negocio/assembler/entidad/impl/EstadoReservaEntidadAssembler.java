package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.negocio.dominio.EstadoReservaDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.UtilObjeto;

public final class EstadoReservaEntidadAssembler implements EntidadAssembler<EstadoReservaDominio, EstadoReservaEntidad> {

    private static EntidadAssembler<EstadoReservaDominio, EstadoReservaEntidad> INSTANCE;

    private EstadoReservaEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<EstadoReservaDominio, EstadoReservaEntidad> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new EstadoReservaEntidadAssembler();
        }
        return INSTANCE;
    }

    @Override
    public EstadoReservaDominio ensamblarDominio(final EstadoReservaEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new EstadoReservaEntidad.Builder().build());
        return new EstadoReservaDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public EstadoReservaEntidad ensamblarEntidad(final EstadoReservaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new EstadoReservaDominio.Builder().build());
        return new EstadoReservaEntidad.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}
