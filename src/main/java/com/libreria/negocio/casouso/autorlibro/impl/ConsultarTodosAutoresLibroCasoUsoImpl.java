package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.autorlibro.ConsultarTodosAutoresLibroCasoUso;

public class ConsultarTodosAutoresLibroCasoUsoImpl implements ConsultarTodosAutoresLibroCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosAutoresLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<AutorLibroEntidad> ejecutar(final AutorLibroEntidad filtro) {
        return daoFactory.getAutorLibroDAO().consultarTodos();
    }
}
