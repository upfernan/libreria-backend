package com.libreria.negocio.fachada.tarifamulta.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.tarifamulta.RetirarTarifaMultaCasoUso;
import com.libreria.negocio.casouso.tarifamulta.impl.RetirarTarifaMultaCasoUsoImpl;
import com.libreria.negocio.fachada.tarifamulta.RetirarTarifaMultaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarTarifaMultaFachadaImpl implements RetirarTarifaMultaFachada {

    private final DAOFactory daoFactory;
    private final RetirarTarifaMultaCasoUso casoUso;

    public RetirarTarifaMultaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RetirarTarifaMultaCasoUsoImpl(daoFactory);
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar la tarifa de multa.", "Error técnico inesperado en RetirarTarifaMultaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
