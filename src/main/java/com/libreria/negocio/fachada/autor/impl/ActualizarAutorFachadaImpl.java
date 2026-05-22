package com.libreria.negocio.fachada.autor.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.negocio.assembler.dto.impl.AutorDTOAssembler;
import com.libreria.negocio.casouso.autor.ActualizarAutorCasoUso;
import com.libreria.negocio.casouso.autor.impl.ActualizarAutorCasoUsoImpl;
import com.libreria.negocio.fachada.autor.ActualizarAutorFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarAutorFachadaImpl implements ActualizarAutorFachada {

    private final DAOFactory daoFactory;
    private final ActualizarAutorCasoUso casoUso;

    public ActualizarAutorFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarAutorCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final AutorDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(AutorDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el autor.", "Error técnico inesperado en ActualizarAutorFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
