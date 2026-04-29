package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.ReservaDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.ReservaDominio;
import com.libreria.transversal.UtilObjeto;

public final class ReservaDTOAssembler implements DTOAssembler<ReservaDominio, ReservaDTO> {

    private static DTOAssembler<ReservaDominio, ReservaDTO> INSTANCE;

    private ReservaDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<ReservaDominio, ReservaDTO> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new ReservaDTOAssembler();
        }
        return INSTANCE;
    }

    @Override
    public ReservaDominio ensamblarDominio(final ReservaDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new ReservaDTO.Builder().build());
        return new ReservaDominio.Builder()
                .id(objeto.getId())
                .fechaReserva(objeto.getFechaReserva())
                .fechaExpiracion(objeto.getFechaExpiracion())
                .estadoReserva(EstadoReservaDTOAssembler.getInstance().ensamblarDominio(objeto.getEstadoReserva()))
                .usuario(UsuarioDTOAssembler.getInstance().ensamblarDominio(objeto.getUsuario()))
                .libro(LibroDTOAssembler.getInstance().ensamblarDominio(objeto.getLibro()))
                .build();
    }

    @Override
    public ReservaDTO ensamblarDTO(final ReservaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new ReservaDominio.Builder().build());
        return new ReservaDTO.Builder()
                .id(objeto.getId())
                .fechaReserva(objeto.getFechaReserva())
                .fechaExpiracion(objeto.getFechaExpiracion())
                .estadoReserva(EstadoReservaDTOAssembler.getInstance().ensamblarDTO(objeto.getEstadoReserva()))
                .usuario(UsuarioDTOAssembler.getInstance().ensamblarDTO(objeto.getUsuario()))
                .libro(LibroDTOAssembler.getInstance().ensamblarDTO(objeto.getLibro()))
                .build();
    }
}
