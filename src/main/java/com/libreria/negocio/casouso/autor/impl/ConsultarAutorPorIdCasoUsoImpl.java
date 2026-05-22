package com.libreria.negocio.casouso.autor.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.casouso.autor.ConsultarAutorPorIdCasoUso;

public class ConsultarAutorPorIdCasoUsoImpl implements ConsultarAutorPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarAutorPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public AutorEntidad ejecutar(final UUID id) {
        // P5 Asegurar que los datos sean válidos a nivel de tipo de dato, formato, longitud y rango
        return daoFactory.getAutorDAO().consultarPorId(id);
    }
}
