package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.TipoLibroDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.TipoLibroDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class TipoLibroDTOAssembler implements DTOAssembler<TipoLibroDominio, TipoLibroDTO> {

    private static DTOAssembler<TipoLibroDominio, TipoLibroDTO> instancia;

    private TipoLibroDTOAssembler() {
        super();
    }

    public static final synchronized DTOAssembler<TipoLibroDominio, TipoLibroDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new TipoLibroDTOAssembler();
        }
        return instancia;
    }

    @Override
    public TipoLibroDominio ensamblarDominio(final TipoLibroDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new TipoLibroDTO.Builder().build());
        return new TipoLibroDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public TipoLibroDTO ensamblarDTO(final TipoLibroDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new TipoLibroDominio.Builder().build());
        return new TipoLibroDTO.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}