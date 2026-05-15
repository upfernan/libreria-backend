package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.autorlibro.ConsultarAutoresPorLibroCasoUso;

public class ConsultarAutoresPorLibroCasoUsoImpl implements ConsultarAutoresPorLibroCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarAutoresPorLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<AutorLibroEntidad> ejecutar(final UUID libroId) {
        // P8 — Consultar todas las relaciones autor-libro del libro indicado
        final AutorLibroEntidad filtro = new AutorLibroEntidad.Builder()
                .libro(new LibroEntidad.Builder().id(libroId).build())
                .build();
        return daoFactory.getAutorLibroDAO().consultarPorFiltro(filtro);
    }
}
