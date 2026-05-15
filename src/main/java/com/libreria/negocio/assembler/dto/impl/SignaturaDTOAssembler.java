package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.SignaturaDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.SignaturaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class SignaturaDTOAssembler implements DTOAssembler<SignaturaDominio, SignaturaDTO> {

    private static DTOAssembler<SignaturaDominio, SignaturaDTO> instancia;

    private SignaturaDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<SignaturaDominio, SignaturaDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new SignaturaDTOAssembler();
        }
        return instancia;
    }

    @Override
    public SignaturaDominio ensamblarDominio(final SignaturaDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new SignaturaDTO.Builder().build());
        return new SignaturaDominio.Builder()
                .id(objeto.getId())
                .pasillo(objeto.getPasillo())
                .estante(objeto.getEstante())
                .posicion(objeto.getPosicion())
                .build();
    }

    @Override
    public SignaturaDTO ensamblarDTO(final SignaturaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new SignaturaDominio.Builder().build());
        return new SignaturaDTO.Builder()
                .id(objeto.getId())
                .pasillo(objeto.getPasillo())
                .estante(objeto.getEstante())
                .posicion(objeto.getPosicion())
                .build();
    }
}