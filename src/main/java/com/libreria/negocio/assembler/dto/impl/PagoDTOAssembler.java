package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.PagoDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.PagoDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class PagoDTOAssembler implements DTOAssembler<PagoDominio, PagoDTO> {

    private static DTOAssembler<PagoDominio, PagoDTO> instancia;

    private PagoDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<PagoDominio, PagoDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new PagoDTOAssembler();
        }
        return instancia;
    }

    @Override
    public PagoDominio ensamblarDominio(final PagoDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new PagoDTO.Builder().build());
        return new PagoDominio.Builder()
                .id(objeto.getId())
                .fechaPago(objeto.getFechaPago())
                .multa(MultaDTOAssembler.getInstance().ensamblarDominio(objeto.getMulta()))
                .build();
    }

    @Override
    public PagoDTO ensamblarDTO(final PagoDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new PagoDominio.Builder().build());
        return new PagoDTO.Builder()
                .id(objeto.getId())
                .fechaPago(objeto.getFechaPago())
                .multa(MultaDTOAssembler.getInstance().ensamblarDTO(objeto.getMulta()))
                .build();
    }
}