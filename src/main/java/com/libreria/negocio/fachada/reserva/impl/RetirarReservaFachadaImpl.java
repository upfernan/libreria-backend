package com.libreria.negocio.fachada.reserva.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.reserva.RetirarReservaCasoUso;
import com.libreria.negocio.casouso.reserva.impl.RetirarReservaCasoUsoImpl;
import com.libreria.negocio.fachada.reserva.RetirarReservaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarReservaFachadaImpl implements RetirarReservaFachada {

    private final DAOFactory daoFactory;
    private final RetirarReservaCasoUso casoUso;

    public RetirarReservaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RetirarReservaCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al eliminar la reserva.", "Error técnico inesperado en RetirarReservaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
