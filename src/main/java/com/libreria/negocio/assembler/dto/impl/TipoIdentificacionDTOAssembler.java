package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.TipoIdentificacionDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.TipoIdentificacionDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class TipoIdentificacionDTOAssembler implements DTOAssembler<TipoIdentificacionDominio, TipoIdentificacionDTO> {

    private static DTOAssembler<TipoIdentificacionDominio, TipoIdentificacionDTO> instancia;

    private TipoIdentificacionDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<TipoIdentificacionDominio, TipoIdentificacionDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new TipoIdentificacionDTOAssembler();
        }
        return instancia;
    }

    @Override
    public TipoIdentificacionDominio ensamblarDominio(final TipoIdentificacionDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new TipoIdentificacionDTO.Builder().build());
        return new TipoIdentificacionDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public TipoIdentificacionDTO ensamblarDTO(final TipoIdentificacionDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new TipoIdentificacionDominio.Builder().build());
        return new TipoIdentificacionDTO.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}