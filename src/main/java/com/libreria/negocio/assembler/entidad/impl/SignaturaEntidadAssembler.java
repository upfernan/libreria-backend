package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.dominio.SignaturaDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class SignaturaEntidadAssembler implements EntidadAssembler<SignaturaDominio, SignaturaEntidad> {

    private static EntidadAssembler<SignaturaDominio, SignaturaEntidad> instancia;

    private SignaturaEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<SignaturaDominio, SignaturaEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new SignaturaEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public SignaturaDominio ensamblarDominio(final SignaturaEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new SignaturaEntidad.Builder().build());
        return new SignaturaDominio.Builder()
                .id(objeto.getId())
                .pasillo(objeto.getPasillo())
                .estante(objeto.getEstante())
                .posicion(objeto.getPosicion())
                .build();
    }

    @Override
    public SignaturaEntidad ensamblarEntidad(final SignaturaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new SignaturaDominio.Builder().build());
        return new SignaturaEntidad.Builder()
                .id(objeto.getId())
                .pasillo(objeto.getPasillo())
                .estante(objeto.getEstante())
                .posicion(objeto.getPosicion())
                .build();
    }
}