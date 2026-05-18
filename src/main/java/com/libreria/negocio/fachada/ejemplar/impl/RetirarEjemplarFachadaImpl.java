package com.libreria.negocio.fachada.ejemplar.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.ejemplar.RetirarEjemplarCasoUso;
import com.libreria.negocio.casouso.ejemplar.impl.RetirarEjemplarCasoUsoImpl;
import com.libreria.negocio.fachada.ejemplar.RetirarEjemplarFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEjemplarFachadaImpl implements RetirarEjemplarFachada {

    private final DAOFactory daoFactory;
    private final RetirarEjemplarCasoUso casoUso;

    public RetirarEjemplarFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RetirarEjemplarCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar el ejemplar.", "Error técnico inesperado en RetirarEjemplarFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
