package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.dominio.PagoDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.UtilObjeto;

public final class PagoEntidadAssembler implements EntidadAssembler<PagoDominio, PagoEntidad> {

    private static EntidadAssembler<PagoDominio, PagoEntidad> INSTANCE;

    private PagoEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<PagoDominio, PagoEntidad> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new PagoEntidadAssembler();
        }
        return INSTANCE;
    }

    @Override
    public PagoDominio ensamblarDominio(final PagoEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new PagoEntidad.Builder().build());
        return new PagoDominio.Builder()
                .id(objeto.getId())
                .fechaPago(objeto.getFechaPago())
                .multa(MultaEntidadAssembler.getInstance().ensamblarDominio(objeto.getMulta()))
                .build();
    }

    @Override
    public PagoEntidad ensamblarEntidad(final PagoDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new PagoDominio.Builder().build());
        return new PagoEntidad.Builder()
                .id(objeto.getId())
                .fechaPago(objeto.getFechaPago())
                .multa(MultaEntidadAssembler.getInstance().ensamblarEntidad(objeto.getMulta()))
                .build();
    }
}
