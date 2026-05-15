package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.dominio.PrestamoDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class PrestamoEntidadAssembler implements EntidadAssembler<PrestamoDominio, PrestamoEntidad> {

    private static EntidadAssembler<PrestamoDominio, PrestamoEntidad> instancia;

    private PrestamoEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<PrestamoDominio, PrestamoEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new PrestamoEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public PrestamoDominio ensamblarDominio(final PrestamoEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new PrestamoEntidad.Builder().build());
        return new PrestamoDominio.Builder()
                .id(objeto.getId())
                .fechaPrestamo(objeto.getFechaPrestamo())
                .fechaDevolucionEsperada(objeto.getFechaDevolucionEsperada())
                .estadoPrestamo(EstadoPrestamoEntidadAssembler.getInstance().ensamblarDominio(objeto.getEstadoPrestamo()))
                .reserva(ReservaEntidadAssembler.getInstance().ensamblarDominio(objeto.getReserva()))
                .usuario(UsuarioEntidadAssembler.getInstance().ensamblarDominio(objeto.getUsuario()))
                .ejemplar(EjemplarEntidadAssembler.getInstance().ensamblarDominio(objeto.getEjemplar()))
                .build();
    }

    @Override
    public PrestamoEntidad ensamblarEntidad(final PrestamoDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new PrestamoDominio.Builder().build());
        return new PrestamoEntidad.Builder()
                .id(objeto.getId())
                .fechaPrestamo(objeto.getFechaPrestamo())
                .fechaDevolucionEsperada(objeto.getFechaDevolucionEsperada())
                .estadoPrestamo(EstadoPrestamoEntidadAssembler.getInstance().ensamblarEntidad(objeto.getEstadoPrestamo()))
                .reserva(ReservaEntidadAssembler.getInstance().ensamblarEntidad(objeto.getReserva()))
                .usuario(UsuarioEntidadAssembler.getInstance().ensamblarEntidad(objeto.getUsuario()))
                .ejemplar(EjemplarEntidadAssembler.getInstance().ensamblarEntidad(objeto.getEjemplar()))
                .build();
    }
}