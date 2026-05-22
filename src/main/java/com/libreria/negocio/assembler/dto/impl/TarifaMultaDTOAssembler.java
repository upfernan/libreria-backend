package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.TarifaMultaDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.TarifaMultaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class TarifaMultaDTOAssembler implements DTOAssembler<TarifaMultaDominio, TarifaMultaDTO> {

    private static DTOAssembler<TarifaMultaDominio, TarifaMultaDTO> instancia;

    private TarifaMultaDTOAssembler() {
        super();
    }

    public static final synchronized DTOAssembler<TarifaMultaDominio, TarifaMultaDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new TarifaMultaDTOAssembler();
        }
        return instancia;
    }

    @Override
    public TarifaMultaDominio ensamblarDominio(final TarifaMultaDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new TarifaMultaDTO.Builder().build());
        return new TarifaMultaDominio.Builder()
                .id(objeto.getId())
                .valorDiario(objeto.getValorDiario())
                .fechaInicioVigencia(objeto.getFechaInicioVigencia())
                .fechaFinVigencia(objeto.getFechaFinVigencia())
                .build();
    }

    @Override
    public TarifaMultaDTO ensamblarDTO(final TarifaMultaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new TarifaMultaDominio.Builder().build());
        return new TarifaMultaDTO.Builder()
                .id(objeto.getId())
                .valorDiario(objeto.getValorDiario())
                .fechaInicioVigencia(objeto.getFechaInicioVigencia())
                .fechaFinVigencia(objeto.getFechaFinVigencia())
                .build();
    }
}
