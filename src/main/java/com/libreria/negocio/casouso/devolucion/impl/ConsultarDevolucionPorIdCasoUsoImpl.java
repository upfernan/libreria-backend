package com.libreria.negocio.casouso.devolucion.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.negocio.casouso.devolucion.ConsultarDevolucionPorIdCasoUso;

public class ConsultarDevolucionPorIdCasoUsoImpl implements ConsultarDevolucionPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarDevolucionPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public DevolucionEntidad ejecutar(final UUID id) {
        // P1 — Delegar la consulta al DAO
        return daoFactory.getDevolucionDAO().consultarPorId(id);
    }
}
