package com.libreria.negocio.fachada.autor.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.autor.RetirarAutorCasoUso;
import com.libreria.negocio.casouso.autor.impl.RetirarAutorCasoUsoImpl;
import com.libreria.negocio.fachada.autor.RetirarAutorFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarAutorFachadaImpl implements RetirarAutorFachada {

    private final DAOFactory daoFactory;
    private final RetirarAutorCasoUso casoUso;

    public RetirarAutorFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RetirarAutorCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar el autor.", "Error técnico inesperado en RetirarAutorFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
