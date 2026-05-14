package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.EditorialDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class EditorialDTOAssembler implements DTOAssembler<EditorialDominio, EditorialDTO> {

    private static DTOAssembler<EditorialDominio, EditorialDTO> instancia;

    private EditorialDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<EditorialDominio, EditorialDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new EditorialDTOAssembler();
        }
        return instancia;
    }

    @Override
    public EditorialDominio ensamblarDominio(final EditorialDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new EditorialDTO.Builder().build());
        return new EditorialDominio.Builder()
                .id(objeto.getId())
                .nit(objeto.getNit())
                .nombre(objeto.getNombre())
                .build();
    }

    @Override
    public EditorialDTO ensamblarDTO(final EditorialDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new EditorialDominio.Builder().build());
        return new EditorialDTO.Builder()
                .id(objeto.getId())
                .nit(objeto.getNit())
                .nombre(objeto.getNombre())
                .build();
    }
}