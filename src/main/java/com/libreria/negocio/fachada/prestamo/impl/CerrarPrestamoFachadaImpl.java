package com.libreria.negocio.fachada.prestamo.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.prestamo.CerrarPrestamoCasoUso;
import com.libreria.negocio.casouso.prestamo.impl.CerrarPrestamoCasoUsoImpl;
import com.libreria.negocio.fachada.prestamo.CerrarPrestamoFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class CerrarPrestamoFachadaImpl implements CerrarPrestamoFachada {

    private final DAOFactory daoFactory;
    private final CerrarPrestamoCasoUso casoUso;

    public CerrarPrestamoFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new CerrarPrestamoCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al cerrar el préstamo.", "Error técnico inesperado en CerrarPrestamoFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
