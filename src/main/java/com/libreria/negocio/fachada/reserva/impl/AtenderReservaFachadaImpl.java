package com.libreria.negocio.fachada.reserva.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.reserva.AtenderReservaCasoUso;
import com.libreria.negocio.casouso.reserva.impl.AtenderReservaCasoUsoImpl;
import com.libreria.negocio.fachada.reserva.AtenderReservaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AtenderReservaFachadaImpl implements AtenderReservaFachada {

    private final DAOFactory daoFactory;
    private final AtenderReservaCasoUso casoUso;

    public AtenderReservaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new AtenderReservaCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final UUID id) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(id);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al atender la reserva.", "Error técnico inesperado en AtenderReservaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
