package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.autorlibro.ConsultarAutorLibroPorIdCasoUso;

public class ConsultarAutorLibroPorIdCasoUsoImpl implements ConsultarAutorLibroPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarAutorLibroPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public AutorLibroEntidad ejecutar(final UUID id) {
        // P8 — Delegar la consulta al DAO
        return daoFactory.getAutorLibroDAO().consultarPorId(id);
    }
}
