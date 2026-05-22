package com.libreria.negocio.casouso.libro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.libro.ConsultarLibroPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarLibroPorIdCasoUsoImpl implements ConsultarLibroPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarLibroPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public LibroEntidad ejecutar(final UUID id) {
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo para consultar libro.");
        }
        return daoFactory.getLibroDAO().consultarPorId(id);
    }

}
