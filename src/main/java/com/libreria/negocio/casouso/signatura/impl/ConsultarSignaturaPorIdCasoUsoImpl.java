package com.libreria.negocio.casouso.signatura.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.ConsultarSignaturaPorIdCasoUso;

public class ConsultarSignaturaPorIdCasoUsoImpl implements ConsultarSignaturaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarSignaturaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public SignaturaEntidad ejecutar(final UUID id) {
        // P7 — Delegar la consulta al DAO
        return daoFactory.getSignaturaDAO().consultarPorId(id);
    }
}
