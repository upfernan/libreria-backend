package com.libreria.negocio.casouso.tarifamulta.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.ConsultarTodasTarifasMultaCasoUso;

public class ConsultarTodasTarifasMultaCasoUsoImpl implements ConsultarTodasTarifasMultaCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasTarifasMultaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<TarifaMultaEntidad> ejecutar(final TarifaMultaEntidad filtro) {
        // P8 — Delegar la consulta al DAO con el filtro recibido
        return daoFactory.getTarifaMultaDAO().consultarPorFiltro(filtro);
    }
}
