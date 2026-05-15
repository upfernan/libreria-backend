package com.libreria.negocio.casouso.pago.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.pago.ConsultarPagoPorIdCasoUso;

public class ConsultarPagoPorIdCasoUsoImpl implements ConsultarPagoPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarPagoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public PagoEntidad ejecutar(final UUID id) {
        // P1 — Delegar la consulta al DAO
        return daoFactory.getPagoDAO().consultarPorId(id);
    }
}
