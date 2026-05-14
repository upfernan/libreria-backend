package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.PrestamoDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.PrestamoDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class PrestamoDTOAssembler implements DTOAssembler<PrestamoDominio, PrestamoDTO> {

    private static DTOAssembler<PrestamoDominio, PrestamoDTO> instancia;

    private PrestamoDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<PrestamoDominio, PrestamoDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new PrestamoDTOAssembler();
        }
        return instancia;
    }

    @Override
    public PrestamoDominio ensamblarDominio(final PrestamoDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new PrestamoDTO.Builder().build());
        return new PrestamoDominio.Builder()
                .id(objeto.getId())
                .fechaPrestamo(objeto.getFechaPrestamo())
                .fechaDevolucionEsperada(objeto.getFechaDevolucionEsperada())
                .estadoPrestamo(EstadoPrestamoDTOAssembler.getInstance().ensamblarDominio(objeto.getEstadoPrestamo()))
                .reserva(ReservaDTOAssembler.getInstance().ensamblarDominio(objeto.getReserva()))
                .usuario(UsuarioDTOAssembler.getInstance().ensamblarDominio(objeto.getUsuario()))
                .ejemplar(EjemplarDTOAssembler.getInstance().ensamblarDominio(objeto.getEjemplar()))
                .build();
    }

    @Override
    public PrestamoDTO ensamblarDTO(final PrestamoDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new PrestamoDominio.Builder().build());
        return new PrestamoDTO.Builder()
                .id(objeto.getId())
                .fechaPrestamo(objeto.getFechaPrestamo())
                .fechaDevolucionEsperada(objeto.getFechaDevolucionEsperada())
                .estadoPrestamo(EstadoPrestamoDTOAssembler.getInstance().ensamblarDTO(objeto.getEstadoPrestamo()))
                .reserva(ReservaDTOAssembler.getInstance().ensamblarDTO(objeto.getReserva()))
                .usuario(UsuarioDTOAssembler.getInstance().ensamblarDTO(objeto.getUsuario()))
                .ejemplar(EjemplarDTOAssembler.getInstance().ensamblarDTO(objeto.getEjemplar()))
                .build();
    }
}