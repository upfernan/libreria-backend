package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.dominio.MultaDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class MultaEntidadAssembler implements EntidadAssembler<MultaDominio, MultaEntidad> {

    private static EntidadAssembler<MultaDominio, MultaEntidad> instancia;

    private MultaEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<MultaDominio, MultaEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new MultaEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public MultaDominio ensamblarDominio(final MultaEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new MultaEntidad.Builder().build());
        return new MultaDominio.Builder()
                .id(objeto.getId())
                .montoTotal(objeto.getMontoTotal())
                .fechaGeneracion(objeto.getFechaGeneracion())
                .pagada(objeto.getPagada())
                .diasRetraso(objeto.getDiasRetraso())
                .tarifaMulta(TarifaMultaEntidadAssembler.getInstance().ensamblarDominio(objeto.getTarifaMulta()))
                .devolucion(DevolucionEntidadAssembler.getInstance().ensamblarDominio(objeto.getDevolucion()))
                .usuarioAfectado(UsuarioEntidadAssembler.getInstance().ensamblarDominio(objeto.getUsuarioAfectado()))
                .build();
    }

    @Override
    public MultaEntidad ensamblarEntidad(final MultaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new MultaDominio.Builder().build());
        return new MultaEntidad.Builder()
                .id(objeto.getId())
                .montoTotal(objeto.getMontoTotal())
                .fechaGeneracion(objeto.getFechaGeneracion())
                .pagada(objeto.getPagada())
                .diasRetraso(objeto.getDiasRetraso())
                .tarifaMulta(TarifaMultaEntidadAssembler.getInstance().ensamblarEntidad(objeto.getTarifaMulta()))
                .devolucion(DevolucionEntidadAssembler.getInstance().ensamblarEntidad(objeto.getDevolucion()))
                .usuarioAfectado(UsuarioEntidadAssembler.getInstance().ensamblarEntidad(objeto.getUsuarioAfectado()))
                .build();
    }
}