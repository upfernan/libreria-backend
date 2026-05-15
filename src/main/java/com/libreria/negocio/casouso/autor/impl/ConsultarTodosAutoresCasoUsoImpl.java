package com.libreria.negocio.casouso.autor.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.casouso.autor.ConsultarTodosAutoresCasoUso;

public class ConsultarTodosAutoresCasoUsoImpl implements ConsultarTodosAutoresCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosAutoresCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<AutorEntidad> ejecutar(final AutorEntidad filtro) {
        // P5 — Delegar la consulta al DAO con el filtro recibido
        return daoFactory.getAutorDAO().consultarPorFiltro(filtro);
    }
}
