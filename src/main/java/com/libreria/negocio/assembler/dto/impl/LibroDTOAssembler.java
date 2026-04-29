package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.LibroDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.transversal.UtilObjeto;

public final class LibroDTOAssembler implements DTOAssembler<LibroDominio, LibroDTO> {

    private static DTOAssembler<LibroDominio, LibroDTO> INSTANCE;

    private LibroDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<LibroDominio, LibroDTO> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new LibroDTOAssembler();
        }
        return INSTANCE;
    }

    @Override
    public LibroDominio ensamblarDominio(final LibroDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new LibroDTO.Builder().build());
        return new LibroDominio.Builder()
                .id(objeto.getId())
                .titulo(objeto.getTitulo())
                .tipoLibro(TipoLibroDTOAssembler.getInstance().ensamblarDominio(objeto.getTipoLibro()))
                .categoria(CategoriaDTOAssembler.getInstance().ensamblarDominio(objeto.getCategoria()))
                .editorial(EditorialDTOAssembler.getInstance().ensamblarDominio(objeto.getEditorial()))
                .disponibles(objeto.getDisponibles())
                .build();
    }

    @Override
    public LibroDTO ensamblarDTO(final LibroDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new LibroDominio.Builder().build());
        return new LibroDTO.Builder()
                .id(objeto.getId())
                .titulo(objeto.getTitulo())
                .tipoLibro(TipoLibroDTOAssembler.getInstance().ensamblarDTO(objeto.getTipoLibro()))
                .categoria(CategoriaDTOAssembler.getInstance().ensamblarDTO(objeto.getCategoria()))
                .editorial(EditorialDTOAssembler.getInstance().ensamblarDTO(objeto.getEditorial()))
                .disponibles(objeto.getDisponibles())
                .build();
    }
}
