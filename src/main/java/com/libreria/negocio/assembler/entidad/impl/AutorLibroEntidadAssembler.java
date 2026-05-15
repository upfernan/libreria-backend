package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.dominio.AutorLibroDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class AutorLibroEntidadAssembler implements EntidadAssembler<AutorLibroDominio, AutorLibroEntidad> {

    private static EntidadAssembler<AutorLibroDominio, AutorLibroEntidad> instancia;

    private AutorLibroEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<AutorLibroDominio, AutorLibroEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new AutorLibroEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public AutorLibroDominio ensamblarDominio(final AutorLibroEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new AutorLibroEntidad.Builder().build());
        return new AutorLibroDominio.Builder()
                .id(objeto.getId())
                .autor(AutorEntidadAssembler.getInstance().ensamblarDominio(objeto.getAutor()))
                .libro(LibroEntidadAssembler.getInstance().ensamblarDominio(objeto.getLibro()))
                .build();
    }

    @Override
    public AutorLibroEntidad ensamblarEntidad(final AutorLibroDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new AutorLibroDominio.Builder().build());
        return new AutorLibroEntidad.Builder()
                .id(objeto.getId())
                .autor(AutorEntidadAssembler.getInstance().ensamblarEntidad(objeto.getAutor()))
                .libro(LibroEntidadAssembler.getInstance().ensamblarEntidad(objeto.getLibro()))
                .build();
    }
}