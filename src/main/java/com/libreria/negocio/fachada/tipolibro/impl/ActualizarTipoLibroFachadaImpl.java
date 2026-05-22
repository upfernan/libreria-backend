package com.libreria.negocio.fachada.tipolibro.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoLibroDTO;
import com.libreria.negocio.assembler.dto.impl.TipoLibroDTOAssembler;
import com.libreria.negocio.casouso.tipolibro.ActualizarTipoLibroCasoUso;
import com.libreria.negocio.casouso.tipolibro.impl.ActualizarTipoLibroCasoUsoImpl;
import com.libreria.negocio.fachada.tipolibro.ActualizarTipoLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarTipoLibroFachadaImpl implements ActualizarTipoLibroFachada {

    private final DAOFactory daoFactory;
    private final ActualizarTipoLibroCasoUso casoUso;

    public ActualizarTipoLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarTipoLibroCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final TipoLibroDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(TipoLibroDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el tipo de libro.", "Error técnico inesperado en ActualizarTipoLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
