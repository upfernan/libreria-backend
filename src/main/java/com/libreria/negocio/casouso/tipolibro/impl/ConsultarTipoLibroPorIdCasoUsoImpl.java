package com.libreria.negocio.casouso.tipolibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.tipolibro.ConsultarTipoLibroPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTipoLibroPorIdCasoUsoImpl implements ConsultarTipoLibroPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTipoLibroPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public TipoLibroEntidad ejecutar(final UUID id) {
        // P6 — Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo para consultar tipo de libro.");
        }
        return daoFactory.getTipoLibroDAO().consultarPorId(id);
    }
}
