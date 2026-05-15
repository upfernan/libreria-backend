package com.libreria.negocio.casouso.editorial.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.casouso.editorial.ConsultarEditorialPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarEditorialPorIdCasoUsoImpl implements ConsultarEditorialPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarEditorialPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public EditorialEntidad ejecutar(final UUID id) {
        // P6 — Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo para consultar editorial.");
        }
        return daoFactory.getEditorialDAO().consultarPorId(id);
    }
}
