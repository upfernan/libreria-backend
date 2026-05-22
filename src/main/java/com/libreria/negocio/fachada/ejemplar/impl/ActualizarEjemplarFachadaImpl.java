package com.libreria.negocio.fachada.ejemplar.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EjemplarDTO;
import com.libreria.negocio.assembler.dto.impl.EjemplarDTOAssembler;
import com.libreria.negocio.casouso.ejemplar.ActualizarEjemplarCasoUso;
import com.libreria.negocio.casouso.ejemplar.impl.ActualizarEjemplarCasoUsoImpl;
import com.libreria.negocio.fachada.ejemplar.ActualizarEjemplarFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEjemplarFachadaImpl implements ActualizarEjemplarFachada {

    private final DAOFactory daoFactory;
    private final ActualizarEjemplarCasoUso casoUso;

    public ActualizarEjemplarFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarEjemplarCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final EjemplarDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(EjemplarDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el ejemplar.", "Error técnico inesperado en ActualizarEjemplarFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
