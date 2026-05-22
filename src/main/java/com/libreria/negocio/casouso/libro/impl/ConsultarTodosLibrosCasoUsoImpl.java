package com.libreria.negocio.casouso.libro.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.libro.ConsultarTodosLibrosCasoUso;

public class ConsultarTodosLibrosCasoUsoImpl implements ConsultarTodosLibrosCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosLibrosCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<LibroEntidad> ejecutar(final LibroEntidad filtro) {
        return daoFactory.getLibroDAO().consultarPorFiltro(filtro);
    }

}
