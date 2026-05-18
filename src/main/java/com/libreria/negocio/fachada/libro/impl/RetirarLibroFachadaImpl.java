package com.libreria.negocio.fachada.libro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.libro.RetirarLibroCasoUso;
import com.libreria.negocio.casouso.libro.impl.RetirarLibroCasoUsoImpl;
import com.libreria.negocio.fachada.libro.RetirarLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarLibroFachadaImpl implements RetirarLibroFachada {

    private final DAOFactory daoFactory;
    private final RetirarLibroCasoUso casoUso;

    public RetirarLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RetirarLibroCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar el libro.", "Error técnico inesperado en RetirarLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
