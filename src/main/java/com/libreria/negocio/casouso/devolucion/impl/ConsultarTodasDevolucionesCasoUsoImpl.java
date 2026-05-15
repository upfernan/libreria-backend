package com.libreria.negocio.casouso.devolucion.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.negocio.casouso.devolucion.ConsultarTodasDevolucionesCasoUso;

public class ConsultarTodasDevolucionesCasoUsoImpl implements ConsultarTodasDevolucionesCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasDevolucionesCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<DevolucionEntidad> ejecutar(final DevolucionEntidad filtro) {
        // P1 — Delegar la consulta al DAO con el filtro recibido
        return daoFactory.getDevolucionDAO().consultarPorFiltro(filtro);
    }
}
