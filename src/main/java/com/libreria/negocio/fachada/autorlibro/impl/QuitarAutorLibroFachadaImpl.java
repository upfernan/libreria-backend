package com.libreria.negocio.fachada.autorlibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.autorlibro.QuitarAutorLibroCasoUso;
import com.libreria.negocio.casouso.autorlibro.impl.QuitarAutorLibroCasoUsoImpl;
import com.libreria.negocio.fachada.autorlibro.QuitarAutorLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class QuitarAutorLibroFachadaImpl implements QuitarAutorLibroFachada {

    private final DAOFactory daoFactory;
    private final QuitarAutorLibroCasoUso casoUso;

    public QuitarAutorLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new QuitarAutorLibroCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al quitar el autor del libro.", "Error técnico inesperado en QuitarAutorLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
