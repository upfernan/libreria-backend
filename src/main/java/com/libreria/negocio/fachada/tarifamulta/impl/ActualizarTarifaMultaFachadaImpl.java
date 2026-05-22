package com.libreria.negocio.fachada.tarifamulta.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.negocio.assembler.dto.impl.TarifaMultaDTOAssembler;
import com.libreria.negocio.casouso.tarifamulta.ActualizarTarifaMultaCasoUso;
import com.libreria.negocio.casouso.tarifamulta.impl.ActualizarTarifaMultaCasoUsoImpl;
import com.libreria.negocio.fachada.tarifamulta.ActualizarTarifaMultaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarTarifaMultaFachadaImpl implements ActualizarTarifaMultaFachada {

    private final DAOFactory daoFactory;
    private final ActualizarTarifaMultaCasoUso casoUso;

    public ActualizarTarifaMultaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarTarifaMultaCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final TarifaMultaDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(TarifaMultaDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar la tarifa de multa.", "Error técnico inesperado en ActualizarTarifaMultaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
