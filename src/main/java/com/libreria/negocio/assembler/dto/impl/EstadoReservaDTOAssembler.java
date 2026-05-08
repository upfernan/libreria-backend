package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.EstadoReservaDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.EstadoReservaDominio;
import com.libreria.transversal.UtilObjeto;

public final class EstadoReservaDTOAssembler implements DTOAssembler<EstadoReservaDominio, EstadoReservaDTO> {

    private static DTOAssembler<EstadoReservaDominio, EstadoReservaDTO> instancia;

    private EstadoReservaDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<EstadoReservaDominio, EstadoReservaDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new EstadoReservaDTOAssembler();
        }
        return instancia;
    }

    @Override
    public EstadoReservaDominio ensamblarDominio(final EstadoReservaDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new EstadoReservaDTO.Builder().build());
        return new EstadoReservaDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public EstadoReservaDTO ensamblarDTO(final EstadoReservaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new EstadoReservaDominio.Builder().build());
        return new EstadoReservaDTO.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}