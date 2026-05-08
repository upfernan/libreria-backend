package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.AutorDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.AutorDominio;
import com.libreria.transversal.UtilObjeto;

public final class AutorDTOAssembler implements DTOAssembler<AutorDominio, AutorDTO> {

    private static DTOAssembler<AutorDominio, AutorDTO> instancia;

    private AutorDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<AutorDominio, AutorDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new AutorDTOAssembler();
        }
        return instancia;
    }

    @Override
    public AutorDominio ensamblarDominio(final AutorDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new AutorDTO.Builder().build());
        return new AutorDominio.Builder()
                .id(objeto.getId())
                .primerNombre(objeto.getPrimerNombre())
                .segundoNombre(objeto.getSegundoNombre())
                .primerApellido(objeto.getPrimerApellido())
                .segundoApellido(objeto.getSegundoApellido())
                .build();
    }

    @Override
    public AutorDTO ensamblarDTO(final AutorDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new AutorDominio.Builder().build());
        return new AutorDTO.Builder()
                .id(objeto.getId())
                .primerNombre(objeto.getPrimerNombre())
                .segundoNombre(objeto.getSegundoNombre())
                .primerApellido(objeto.getPrimerApellido())
                .segundoApellido(objeto.getSegundoApellido())
                .build();
    }
}