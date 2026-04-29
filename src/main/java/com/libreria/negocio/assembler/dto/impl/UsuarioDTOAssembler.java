package com.libreria.negocio.assembler.dto.impl;

import com.libreria.dto.UsuarioDTO;
import com.libreria.negocio.assembler.dto.DTOAssembler;
import com.libreria.negocio.dominio.UsuarioDominio;
import com.libreria.transversal.UtilObjeto;

public final class UsuarioDTOAssembler implements DTOAssembler<UsuarioDominio, UsuarioDTO> {

    private static DTOAssembler<UsuarioDominio, UsuarioDTO> INSTANCE;

    private UsuarioDTOAssembler() {
        super();
    }

    public synchronized static final DTOAssembler<UsuarioDominio, UsuarioDTO> getInstance() {
        if (UtilObjeto.esNulo(INSTANCE)) {
            INSTANCE = new UsuarioDTOAssembler();
        }
        return INSTANCE;
    }

    @Override
    public UsuarioDominio ensamblarDominio(final UsuarioDTO dto) {
        var objeto = UtilObjeto.obtenerValorDefecto(dto, new UsuarioDTO.Builder().build());
        return new UsuarioDominio.Builder()
                .id(objeto.getId())
                .tipoIdentificacion(TipoIdentificacionDTOAssembler.getInstance().ensamblarDominio(objeto.getTipoIdentificacion()))
                .numeroIdentificacion(objeto.getNumeroIdentificacion())
                .primerNombre(objeto.getPrimerNombre())
                .segundoNombre(objeto.getSegundoNombre())
                .primerApellido(objeto.getPrimerApellido())
                .segundoApellido(objeto.getSegundoApellido())
                .correoElectronico(objeto.getCorreoElectronico())
                .build();
    }

    @Override
    public UsuarioDTO ensamblarDTO(final UsuarioDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new UsuarioDominio.Builder().build());
        return new UsuarioDTO.Builder()
                .id(objeto.getId())
                .tipoIdentificacion(TipoIdentificacionDTOAssembler.getInstance().ensamblarDTO(objeto.getTipoIdentificacion()))
                .numeroIdentificacion(objeto.getNumeroIdentificacion())
                .primerNombre(objeto.getPrimerNombre())
                .segundoNombre(objeto.getSegundoNombre())
                .primerApellido(objeto.getPrimerApellido())
                .segundoApellido(objeto.getSegundoApellido())
                .correoElectronico(objeto.getCorreoElectronico())
                .build();
    }
}
