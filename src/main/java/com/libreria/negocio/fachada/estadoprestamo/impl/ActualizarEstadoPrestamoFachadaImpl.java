package com.libreria.negocio.fachada.estadoprestamo.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EstadoPrestamoDTO;
import com.libreria.negocio.assembler.dto.impl.EstadoPrestamoDTOAssembler;
import com.libreria.negocio.casouso.estadoprestamo.ActualizarEstadoPrestamoCasoUso;
import com.libreria.negocio.casouso.estadoprestamo.impl.ActualizarEstadoPrestamoCasoUsoImpl;
import com.libreria.negocio.fachada.estadoprestamo.ActualizarEstadoPrestamoFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEstadoPrestamoFachadaImpl implements ActualizarEstadoPrestamoFachada {

    private final DAOFactory daoFactory;
    private final ActualizarEstadoPrestamoCasoUso casoUso;

    public ActualizarEstadoPrestamoFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarEstadoPrestamoCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final EstadoPrestamoDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(EstadoPrestamoDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el estado de préstamo.", "Error técnico inesperado en ActualizarEstadoPrestamoFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
