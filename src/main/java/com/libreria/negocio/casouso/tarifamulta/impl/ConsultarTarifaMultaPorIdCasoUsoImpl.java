package com.libreria.negocio.casouso.tarifamulta.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.ConsultarTarifaMultaPorIdCasoUso;

public class ConsultarTarifaMultaPorIdCasoUsoImpl implements ConsultarTarifaMultaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTarifaMultaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public TarifaMultaEntidad ejecutar(final UUID id) {
        // P8 — Delegar la consulta al DAO
        return daoFactory.getTarifaMultaDAO().consultarPorId(id);
    }
}
