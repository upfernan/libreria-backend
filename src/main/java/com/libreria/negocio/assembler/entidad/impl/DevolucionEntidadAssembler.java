package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.DevolucionEntidad;
import com.libreria.negocio.dominio.DevolucionDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class DevolucionEntidadAssembler implements EntidadAssembler<DevolucionDominio, DevolucionEntidad> {

    private static EntidadAssembler<DevolucionDominio, DevolucionEntidad> instancia;

    private DevolucionEntidadAssembler() {
        super();
    }

    public static final synchronized EntidadAssembler<DevolucionDominio, DevolucionEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new DevolucionEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public DevolucionDominio ensamblarDominio(final DevolucionEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new DevolucionEntidad.Builder().build());
        return new DevolucionDominio.Builder()
                .id(objeto.getId())
                .fechaDevolucion(objeto.getFechaDevolucion())
                .prestamo(PrestamoEntidadAssembler.getInstance().ensamblarDominio(objeto.getPrestamo()))
                .build();
    }

    @Override
    public DevolucionEntidad ensamblarEntidad(final DevolucionDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new DevolucionDominio.Builder().build());
        return new DevolucionEntidad.Builder()
                .id(objeto.getId())
                .fechaDevolucion(objeto.getFechaDevolucion())
                .prestamo(PrestamoEntidadAssembler.getInstance().ensamblarEntidad(objeto.getPrestamo()))
                .build();
    }
}