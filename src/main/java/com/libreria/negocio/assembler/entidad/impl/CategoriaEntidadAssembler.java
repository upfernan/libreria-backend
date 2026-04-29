package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.CategoriaEntidad;
import com.libreria.negocio.dominio.CategoriaDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.UtilObjeto;

public final class CategoriaEntidadAssembler implements EntidadAssembler<CategoriaDominio, CategoriaEntidad> {

    private static EntidadAssembler<CategoriaDominio, CategoriaEntidad> INSTANCE;

    private CategoriaEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<CategoriaDominio, CategoriaEntidad> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new CategoriaEntidadAssembler();
        }
        return INSTANCE;
    }

    @Override
    public CategoriaDominio ensamblarDominio(final CategoriaEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new CategoriaEntidad.Builder().build());
        return new CategoriaDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public CategoriaEntidad ensamblarEntidad(final CategoriaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new CategoriaDominio.Builder().build());
        return new CategoriaEntidad.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}
