package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.EjemplarDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.EjemplarDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class EjemplarDTOAssembler implements DTOAssembler<EjemplarDominio, EjemplarDTO> {

    private static DTOAssembler<EjemplarDominio, EjemplarDTO> instancia;

    private EjemplarDTOAssembler() {
        super();
    }

    public static final synchronized DTOAssembler<EjemplarDominio, EjemplarDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new EjemplarDTOAssembler();
        }
        return instancia;
    }

    @Override
    public EjemplarDominio ensamblarDominio(final EjemplarDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new EjemplarDTO.Builder().build());
        return new EjemplarDominio.Builder()
                .id(objeto.getId())
                .libro(LibroDTOAssembler.getInstance().ensamblarDominio(objeto.getLibro()))
                .signatura(SignaturaDTOAssembler.getInstance().ensamblarDominio(objeto.getSignatura()))
                .build();
    }

    @Override
    public EjemplarDTO ensamblarDTO(final EjemplarDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new EjemplarDominio.Builder().build());
        return new EjemplarDTO.Builder()
                .id(objeto.getId())
                .libro(LibroDTOAssembler.getInstance().ensamblarDTO(objeto.getLibro()))
                .signatura(SignaturaDTOAssembler.getInstance().ensamblarDTO(objeto.getSignatura()))
                .build();
    }
}