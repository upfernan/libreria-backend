package com.libreria.negocio.fachada.signatura.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.signatura.RetirarSignaturaCasoUso;
import com.libreria.negocio.casouso.signatura.impl.RetirarSignaturaCasoUsoImpl;
import com.libreria.negocio.fachada.signatura.RetirarSignaturaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarSignaturaFachadaImpl implements RetirarSignaturaFachada {

    private final DAOFactory daoFactory;
    private final RetirarSignaturaCasoUso casoUso;

    public RetirarSignaturaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RetirarSignaturaCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar la signatura.", "Error técnico inesperado en RetirarSignaturaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
