package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.dominio.ReservaDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class ReservaEntidadAssembler implements EntidadAssembler<ReservaDominio, ReservaEntidad> {

    private static EntidadAssembler<ReservaDominio, ReservaEntidad> instancia;

    private ReservaEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<ReservaDominio, ReservaEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new ReservaEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public ReservaDominio ensamblarDominio(final ReservaEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new ReservaEntidad.Builder().build());
        return new ReservaDominio.Builder()
                .id(objeto.getId())
                .fechaReserva(objeto.getFechaReserva())
                .fechaExpiracion(objeto.getFechaExpiracion())
                .estadoReserva(EstadoReservaEntidadAssembler.getInstance().ensamblarDominio(objeto.getEstadoReserva()))
                .usuario(UsuarioEntidadAssembler.getInstance().ensamblarDominio(objeto.getUsuario()))
                .libro(LibroEntidadAssembler.getInstance().ensamblarDominio(objeto.getLibro()))
                .build();
    }

    @Override
    public ReservaEntidad ensamblarEntidad(final ReservaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new ReservaDominio.Builder().build());
        return new ReservaEntidad.Builder()
                .id(objeto.getId())
                .fechaReserva(objeto.getFechaReserva())
                .fechaExpiracion(objeto.getFechaExpiracion())
                .estadoReserva(EstadoReservaEntidadAssembler.getInstance().ensamblarEntidad(objeto.getEstadoReserva()))
                .usuario(UsuarioEntidadAssembler.getInstance().ensamblarEntidad(objeto.getUsuario()))
                .libro(LibroEntidadAssembler.getInstance().ensamblarEntidad(objeto.getLibro()))
                .build();
    }
}