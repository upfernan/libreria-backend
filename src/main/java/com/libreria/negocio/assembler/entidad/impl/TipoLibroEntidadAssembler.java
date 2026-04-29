package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.dominio.TipoLibroDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.UtilObjeto;

public final class TipoLibroEntidadAssembler implements EntidadAssembler<TipoLibroDominio, TipoLibroEntidad> {

    private static EntidadAssembler<TipoLibroDominio, TipoLibroEntidad> INSTANCE;

    private TipoLibroEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<TipoLibroDominio, TipoLibroEntidad> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new TipoLibroEntidadAssembler();
        }
        return INSTANCE;
    }

    @Override
    public TipoLibroDominio ensamblarDominio(final TipoLibroEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new TipoLibroEntidad.Builder().build());
        return new TipoLibroDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public TipoLibroEntidad ensamblarEntidad(final TipoLibroDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new TipoLibroDominio.Builder().build());
        return new TipoLibroEntidad.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}
