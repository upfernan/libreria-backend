package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.EstadoPrestamoDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.EstadoPrestamoDominio;
import com.libreria.transversal.UtilObjeto;

public final class EstadoPrestamoDTOAssembler implements DTOAssembler<EstadoPrestamoDominio, EstadoPrestamoDTO> {

    private static DTOAssembler<EstadoPrestamoDominio, EstadoPrestamoDTO> instancia;

    private EstadoPrestamoDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<EstadoPrestamoDominio, EstadoPrestamoDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new EstadoPrestamoDTOAssembler();
        }
        return instancia;
    }

    @Override
    public EstadoPrestamoDominio ensamblarDominio(final EstadoPrestamoDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new EstadoPrestamoDTO.Builder().build());
        return new EstadoPrestamoDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public EstadoPrestamoDTO ensamblarDTO(final EstadoPrestamoDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new EstadoPrestamoDominio.Builder().build());
        return new EstadoPrestamoDTO.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}