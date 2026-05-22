package com.libreria.negocio.casouso.tipoidentificacion.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.negocio.casouso.tipoidentificacion.ConsultarTipoIdentificacionPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTipoIdentificacionPorIdCasoUsoImpl implements ConsultarTipoIdentificacionPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTipoIdentificacionPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public TipoIdentificacionEntidad ejecutar(final UUID id) {
        // P6 — Asegura que los datos requeridos sean validos a nivel de tipo, formato y obligatoriedad
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo para consultar tipo de identificación.");
        }
        return daoFactory.getTipoIdentificacionDAO().consultarPorId(id);
    }
}
