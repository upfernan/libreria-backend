package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.DevolucionDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.DevolucionDominio;
import com.libreria.transversal.UtilObjeto;

public final class DevolucionDTOAssembler implements DTOAssembler<DevolucionDominio, DevolucionDTO> {

    private static DTOAssembler<DevolucionDominio, DevolucionDTO> INSTANCE;

    private DevolucionDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<DevolucionDominio, DevolucionDTO> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new DevolucionDTOAssembler();
        }
        return INSTANCE;
    }

    @Override
    public DevolucionDominio ensamblarDominio(final DevolucionDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new DevolucionDTO.Builder().build());
        return new DevolucionDominio.Builder()
                .id(objeto.getId())
                .fechaDevolucion(objeto.getFechaDevolucion())
                .prestamo(PrestamoDTOAssembler.getInstance().ensamblarDominio(objeto.getPrestamo()))
                .build();
    }

    @Override
    public DevolucionDTO ensamblarDTO(final DevolucionDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new DevolucionDominio.Builder().build());
        return new DevolucionDTO.Builder()
                .id(objeto.getId())
                .fechaDevolucion(objeto.getFechaDevolucion())
                .prestamo(PrestamoDTOAssembler.getInstance().ensamblarDTO(objeto.getPrestamo()))
                .build();
    }
}
