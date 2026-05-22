package com.libreria.negocio.casouso.multa.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.casouso.multa.ConsultarTodasMultasCasoUso;

public class ConsultarTodasMultasCasoUsoImpl implements ConsultarTodasMultasCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasMultasCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<MultaEntidad> ejecutar(final MultaEntidad filtro) {
        return daoFactory.getMultaDAO().consultarTodos();
    }
}
