package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.MultaDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.MultaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class MultaDTOAssembler implements DTOAssembler<MultaDominio, MultaDTO> {

    private static DTOAssembler<MultaDominio, MultaDTO> instancia;

    private MultaDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<MultaDominio, MultaDTO> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new MultaDTOAssembler();
        }
        return instancia;
    }

    @Override
    public MultaDominio ensamblarDominio(final MultaDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new MultaDTO.Builder().build());
        return new MultaDominio.Builder()
                .id(objeto.getId())
                .montoTotal(objeto.getMontoTotal())
                .fechaGeneracion(objeto.getFechaGeneracion())
                .pagada(objeto.getPagada())
                .diasRetraso(objeto.getDiasRetraso())
                .tarifaMulta(TarifaMultaDTOAssembler.getInstance().ensamblarDominio(objeto.getTarifaMulta()))
                .devolucion(DevolucionDTOAssembler.getInstance().ensamblarDominio(objeto.getDevolucion()))
                .usuarioAfectado(UsuarioDTOAssembler.getInstance().ensamblarDominio(objeto.getUsuarioAfectado()))
                .build();
    }

    @Override
    public MultaDTO ensamblarDTO(final MultaDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new MultaDominio.Builder().build());
        return new MultaDTO.Builder()
                .id(objeto.getId())
                .montoTotal(objeto.getMontoTotal())
                .fechaGeneracion(objeto.getFechaGeneracion())
                .pagada(objeto.getPagada())
                .diasRetraso(objeto.getDiasRetraso())
                .tarifaMulta(TarifaMultaDTOAssembler.getInstance().ensamblarDTO(objeto.getTarifaMulta()))
                .devolucion(DevolucionDTOAssembler.getInstance().ensamblarDTO(objeto.getDevolucion()))
                .usuarioAfectado(UsuarioDTOAssembler.getInstance().ensamblarDTO(objeto.getUsuarioAfectado()))
                .build();
    }
}