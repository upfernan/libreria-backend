package com.libreria.negocio.assembler.entidad.impl;

import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.negocio.assembler.entidad.EntidadAssembler;
import com.libreria.transversal.utilitario.UtilObjeto;

public final class LibroEntidadAssembler implements EntidadAssembler<LibroDominio, LibroEntidad> {

    private static EntidadAssembler<LibroDominio, LibroEntidad> instancia;

    private LibroEntidadAssembler() {
        super();
    }

    public synchronized static final EntidadAssembler<LibroDominio, LibroEntidad> getInstance() {
        if (UtilObjeto.esNulo(instancia)) {
            instancia = new LibroEntidadAssembler();
        }
        return instancia;
    }

    @Override
    public LibroDominio ensamblarDominio(final LibroEntidad entidad) {
        var objeto = UtilObjeto.obtenerValorDefecto(entidad, new LibroEntidad.Builder().build());
        return new LibroDominio.Builder()
                .id(objeto.getId())
                .titulo(objeto.getTitulo())
                .tipoLibro(TipoLibroEntidadAssembler.getInstance().ensamblarDominio(objeto.getTipoLibro()))
                .categoria(CategoriaEntidadAssembler.getInstance().ensamblarDominio(objeto.getCategoria()))
                .editorial(EditorialEntidadAssembler.getInstance().ensamblarDominio(objeto.getEditorial()))
                .disponibles(objeto.getDisponibles())
                .build();
    }

    @Override
    public LibroEntidad ensamblarEntidad(final LibroDominio dominio) {
        var objeto = UtilObjeto.obtenerValorDefecto(dominio, new LibroDominio.Builder().build());
        return new LibroEntidad.Builder()
                .id(objeto.getId())
                .titulo(objeto.getTitulo())
                .tipoLibro(TipoLibroEntidadAssembler.getInstance().ensamblarEntidad(objeto.getTipoLibro()))
                .categoria(CategoriaEntidadAssembler.getInstance().ensamblarEntidad(objeto.getCategoria()))
                .editorial(EditorialEntidadAssembler.getInstance().ensamblarEntidad(objeto.getEditorial()))
                .disponibles(objeto.getDisponibles())
                .build();
    }
}