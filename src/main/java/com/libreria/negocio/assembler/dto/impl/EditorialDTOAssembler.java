package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.EditorialDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.transversal.UtilObjeto;

public final class EditorialDTOAssembler implements DTOAssembler<EditorialDominio, EditorialDTO> {

    private static DTOAssembler<EditorialDominio, EditorialDTO> INSTANCE;

    private EditorialDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<EditorialDominio, EditorialDTO> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new EditorialDTOAssembler();
        }
        return INSTANCE;
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
