package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.EjemplarEntidad;
import com.libreria.negocio.dominio.EjemplarDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class EjemplarEntidadAssembler implements EntidadAssembler<EjemplarDominio, EjemplarEntidad> {

    private static EntidadAssembler<EjemplarDominio, EjemplarEntidad> instancia;

    private EjemplarEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<EjemplarDominio, EjemplarEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new EjemplarEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public EjemplarDominio ensamblarDominio(final EjemplarEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new EjemplarEntidad.Builder().build());
        return new EjemplarDominio.Builder()
                .id(objeto.getId())
                .libro(LibroEntidadAssembler.getInstance().ensamblarDominio(objeto.getLibro()))
                .signatura(SignaturaEntidadAssembler.getInstance().ensamblarDominio(objeto.getSignatura()))
                .build();
    }

    @Override
    public EjemplarEntidad ensamblarEntidad(final EjemplarDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new EjemplarDominio.Builder().build());
        return new EjemplarEntidad.Builder()
                .id(objeto.getId())
                .libro(LibroEntidadAssembler.getInstance().ensamblarEntidad(objeto.getLibro()))
                .signatura(SignaturaEntidadAssembler.getInstance().ensamblarEntidad(objeto.getSignatura()))
                .build();
    }
}