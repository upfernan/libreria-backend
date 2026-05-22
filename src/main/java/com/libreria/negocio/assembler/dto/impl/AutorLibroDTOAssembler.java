package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.AutorLibroDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.AutorLibroDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class AutorLibroDTOAssembler implements DTOAssembler<AutorLibroDominio, AutorLibroDTO> {

    private static DTOAssembler<AutorLibroDominio, AutorLibroDTO> instancia;

    private AutorLibroDTOAssembler() {
        super();
    }

    public static final synchronized DTOAssembler<AutorLibroDominio, AutorLibroDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new AutorLibroDTOAssembler();
        }
        return instancia;
    }

    @Override
    public AutorLibroDominio ensamblarDominio(final AutorLibroDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new AutorLibroDTO.Builder().build());
        return new AutorLibroDominio.Builder()
                .id(objeto.getId())
                .autor(AutorDTOAssembler.getInstance().ensamblarDominio(objeto.getAutor()))
                .libro(LibroDTOAssembler.getInstance().ensamblarDominio(objeto.getLibro()))
                .build();
    }

    @Override
    public AutorLibroDTO ensamblarDTO(final AutorLibroDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new AutorLibroDominio.Builder().build());
        return new AutorLibroDTO.Builder()
                .id(objeto.getId())
                .autor(AutorDTOAssembler.getInstance().ensamblarDTO(objeto.getAutor()))
                .libro(LibroDTOAssembler.getInstance().ensamblarDTO(objeto.getLibro()))
                .build();
    }
}