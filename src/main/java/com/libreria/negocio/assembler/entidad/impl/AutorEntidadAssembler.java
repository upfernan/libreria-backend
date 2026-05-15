package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.dominio.AutorDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class AutorEntidadAssembler implements EntidadAssembler<AutorDominio, AutorEntidad> {

    private static EntidadAssembler<AutorDominio, AutorEntidad> instancia;

    private AutorEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<AutorDominio, AutorEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new AutorEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public AutorDominio ensamblarDominio(final AutorEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new AutorEntidad.Builder().build());
        return new AutorDominio.Builder()
                .id(objeto.getId())
                .primerNombre(objeto.getPrimerNombre())
                .segundoNombre(objeto.getSegundoNombre())
                .primerApellido(objeto.getPrimerApellido())
                .segundoApellido(objeto.getSegundoApellido())
                .build();
    }

    @Override
    public AutorEntidad ensamblarEntidad(final AutorDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new AutorDominio.Builder().build());
        return new AutorEntidad.Builder()
                .id(objeto.getId())
                .primerNombre(objeto.getPrimerNombre())
                .segundoNombre(objeto.getSegundoNombre())
                .primerApellido(objeto.getPrimerApellido())
                .segundoApellido(objeto.getSegundoApellido())
                .build();
    }
}