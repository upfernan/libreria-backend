package com.libreria.negocio.casouso.multa.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.casouso.multa.ConsultarMultaPorIdCasoUso;

public class ConsultarMultaPorIdCasoUsoImpl implements ConsultarMultaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarMultaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public MultaEntidad ejecutar(final UUID id) {
        // P1 — Delegar la consulta al DAO
        return daoFactory.getMultaDAO().consultarPorId(id);
    }
}
