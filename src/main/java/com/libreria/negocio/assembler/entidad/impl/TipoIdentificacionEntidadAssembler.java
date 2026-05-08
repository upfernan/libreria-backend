package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.negocio.dominio.TipoIdentificacionDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.UtilObjeto;

public final class TipoIdentificacionEntidadAssembler implements EntidadAssembler<TipoIdentificacionDominio, TipoIdentificacionEntidad> {

    private static EntidadAssembler<TipoIdentificacionDominio, TipoIdentificacionEntidad> instancia;

    private TipoIdentificacionEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<TipoIdentificacionDominio, TipoIdentificacionEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new TipoIdentificacionEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public TipoIdentificacionDominio ensamblarDominio(final TipoIdentificacionEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new TipoIdentificacionEntidad.Builder().build());
        return new TipoIdentificacionDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public TipoIdentificacionEntidad ensamblarEntidad(final TipoIdentificacionDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new TipoIdentificacionDominio.Builder().build());
        return new TipoIdentificacionEntidad.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}