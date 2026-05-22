package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class EditorialEntidadAssembler implements EntidadAssembler<EditorialDominio, EditorialEntidad> {

    private static EntidadAssembler<EditorialDominio, EditorialEntidad> instancia;

    private EditorialEntidadAssembler() {
        super();
    }

    public static final synchronized EntidadAssembler<EditorialDominio, EditorialEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new EditorialEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public EditorialDominio ensamblarDominio(final EditorialEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new EditorialEntidad.Builder().build());
        return new EditorialDominio.Builder()
                .id(objeto.getId())
                .nit(objeto.getNit())
                .nombre(objeto.getNombre())
                .build();
    }

    @Override
    public EditorialEntidad ensamblarEntidad(final EditorialDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new EditorialDominio.Builder().build());
        return new EditorialEntidad.Builder()
                .id(objeto.getId())
                .nit(objeto.getNit())
                .nombre(objeto.getNombre())
                .build();
    }
}