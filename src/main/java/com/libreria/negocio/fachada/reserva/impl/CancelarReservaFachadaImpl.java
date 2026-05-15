package com.libreria.negocio.fachada.reserva.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.reserva.CancelarReservaCasoUso;
import com.libreria.negocio.casouso.reserva.impl.CancelarReservaCasoUsoImpl;
import com.libreria.negocio.fachada.reserva.CancelarReservaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class CancelarReservaFachadaImpl implements CancelarReservaFachada {

    private final DAOFactory daoFactory;
    private final CancelarReservaCasoUso casoUso;

    public CancelarReservaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new CancelarReservaCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al cancelar la reserva.", "Error técnico inesperado en CancelarReservaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
