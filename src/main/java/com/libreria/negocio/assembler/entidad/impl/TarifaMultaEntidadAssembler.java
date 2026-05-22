package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.dominio.TarifaMultaDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class TarifaMultaEntidadAssembler implements EntidadAssembler<TarifaMultaDominio, TarifaMultaEntidad> {

    private static EntidadAssembler<TarifaMultaDominio, TarifaMultaEntidad> instancia;

    private TarifaMultaEntidadAssembler() {
        super();
    }

    public static final synchronized EntidadAssembler<TarifaMultaDominio, TarifaMultaEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new TarifaMultaEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public TarifaMultaDominio ensamblarDominio(final TarifaMultaEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new TarifaMultaEntidad.Builder().build());
        return new TarifaMultaDominio.Builder()
                .id(objeto.getId())
                .valorDiario(objeto.getValorDiario())
                .fechaInicioVigencia(objeto.getFechaInicioVigencia())
                .fechaFinVigencia(objeto.getFechaFinVigencia())
                .build();
    }

    @Override
    public TarifaMultaEntidad ensamblarEntidad(final TarifaMultaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new TarifaMultaDominio.Builder().build());
        return new TarifaMultaEntidad.Builder()
                .id(objeto.getId())
                .valorDiario(objeto.getValorDiario())
                .fechaInicioVigencia(objeto.getFechaInicioVigencia())
                .fechaFinVigencia(objeto.getFechaFinVigencia())
                .build();
    }
}
