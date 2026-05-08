package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.dominio.UsuarioDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.UtilObjeto;

public final class UsuarioEntidadAssembler implements EntidadAssembler<UsuarioDominio, UsuarioEntidad> {

    private static EntidadAssembler<UsuarioDominio, UsuarioEntidad> instancia;

    private UsuarioEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<UsuarioDominio, UsuarioEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new UsuarioEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public UsuarioDominio ensamblarDominio(final UsuarioEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new UsuarioEntidad.Builder().build());
        return new UsuarioDominio.Builder()
                .id(objeto.getId())
                .tipoIdentificacion(TipoIdentificacionEntidadAssembler.getInstance().ensamblarDominio(objeto.getTipoIdentificacion()))
                .numeroIdentificacion(objeto.getNumeroIdentificacion())
                .primerNombre(objeto.getPrimerNombre())
                .segundoNombre(objeto.getSegundoNombre())
                .primerApellido(objeto.getPrimerApellido())
                .segundoApellido(objeto.getSegundoApellido())
                .correoElectronico(objeto.getCorreoElectronico())
                .build();
    }

    @Override
    public UsuarioEntidad ensamblarEntidad(final UsuarioDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new UsuarioDominio.Builder().build());
        return new UsuarioEntidad.Builder()
                .id(objeto.getId())
                .tipoIdentificacion(TipoIdentificacionEntidadAssembler.getInstance().ensamblarEntidad(objeto.getTipoIdentificacion()))
                .numeroIdentificacion(objeto.getNumeroIdentificacion())
                .primerNombre(objeto.getPrimerNombre())
                .segundoNombre(objeto.getSegundoNombre())
                .primerApellido(objeto.getPrimerApellido())
                .segundoApellido(objeto.getSegundoApellido())
                .correoElectronico(objeto.getCorreoElectronico())
                .build();
    }
}