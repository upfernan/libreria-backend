package com.libreria.negocio.fachada.reserva.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.ReservaDTO;
import com.libreria.negocio.assembler.dto.impl.ReservaDTOAssembler;
import com.libreria.negocio.casouso.reserva.RegistrarReservaCasoUso;
import com.libreria.negocio.casouso.reserva.impl.RegistrarReservaCasoUsoImpl;
import com.libreria.negocio.fachada.reserva.RegistrarReservaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarReservaFachadaImpl implements RegistrarReservaFachada {

    private DAOFactory daoFactory;
    private RegistrarReservaCasoUso casoUso;

    public RegistrarReservaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RegistrarReservaCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final ReservaDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(ReservaDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar la reserva.", "Error técnico inesperado en RegistrarReservaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
