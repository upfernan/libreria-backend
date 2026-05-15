package com.libreria.negocio.casouso.reserva.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.casouso.reserva.ConsultarReservaPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarReservaPorIdCasoUsoImpl implements ConsultarReservaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarReservaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public ReservaEntidad ejecutar(final UUID id) {
        // Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo para consultar reserva.");
        }
        return daoFactory.getReservaDAO().consultarPorId(id);
    }

}
