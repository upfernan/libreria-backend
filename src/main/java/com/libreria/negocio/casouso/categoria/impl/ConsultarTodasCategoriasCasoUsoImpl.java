package com.libreria.negocio.casouso.categoria.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.negocio.casouso.categoria.ConsultarTodasCategoriasCasoUso;

public class ConsultarTodasCategoriasCasoUsoImpl implements ConsultarTodasCategoriasCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasCategoriasCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<CategoriaEntidad> ejecutar(final CategoriaEntidad filtro) {
        // P6 — El filtro puede ser nulo (retorna todas), simplemente se delega al DAO
        return daoFactory.getCategoriaDAO().consultarPorFiltro(filtro);
    }
}
