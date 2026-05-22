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
        // P5 asegurar que el identificador único (ID) que se envía para llevar a cabo la acción sea válido a nivel de formato.
        return daoFactory.getMultaDAO().consultarPorId(id);
    }
}
