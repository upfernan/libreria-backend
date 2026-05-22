package com.libreria.negocio.fachada.libro.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.LibroDTO;
import com.libreria.negocio.assembler.dto.impl.LibroDTOAssembler;
import com.libreria.negocio.casouso.libro.RegistrarLibroCasoUso;
import com.libreria.negocio.casouso.libro.impl.RegistrarLibroCasoUsoImpl;
import com.libreria.negocio.fachada.libro.RegistrarLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarLibroFachadaImpl implements RegistrarLibroFachada {

    private DAOFactory daoFactory;
    private RegistrarLibroCasoUso casoUso;

    public RegistrarLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RegistrarLibroCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final LibroDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(LibroDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar el libro.", "Error técnico inesperado en RegistrarLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
