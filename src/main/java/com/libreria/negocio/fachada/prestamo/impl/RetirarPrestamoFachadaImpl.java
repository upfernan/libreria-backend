package com.libreria.negocio.fachada.prestamo.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.prestamo.RetirarPrestamoCasoUso;
import com.libreria.negocio.casouso.prestamo.impl.RetirarPrestamoCasoUsoImpl;
import com.libreria.negocio.fachada.prestamo.RetirarPrestamoFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarPrestamoFachadaImpl implements RetirarPrestamoFachada {

    private final DAOFactory daoFactory;
    private final RetirarPrestamoCasoUso casoUso;

    public RetirarPrestamoFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RetirarPrestamoCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al eliminar el préstamo.", "Error técnico inesperado en RetirarPrestamoFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
