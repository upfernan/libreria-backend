package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.CategoriaDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.CategoriaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class CategoriaDTOAssembler implements DTOAssembler<CategoriaDominio, CategoriaDTO> {

    private static DTOAssembler<CategoriaDominio, CategoriaDTO> instancia;

    private CategoriaDTOAssembler() {
        super();
    }

    public static final synchronized DTOAssembler<CategoriaDominio, CategoriaDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new CategoriaDTOAssembler();
        }
        return instancia;
    }

    @Override
    public CategoriaDominio ensamblarDominio(final CategoriaDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new CategoriaDTO.Builder().build());
        return new CategoriaDominio.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }

    @Override
    public CategoriaDTO ensamblarDTO(final CategoriaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new CategoriaDominio.Builder().build());
        return new CategoriaDTO.Builder()
                .id(objeto.getId())
                .nombre(objeto.getNombre())
                .descripcion(objeto.getDescripcion())
                .build();
    }
}