package com.libreria.negocio.fachada.libro.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.LibroDTO;
import com.libreria.negocio.casouso.libro.ActualizarLibroCasoUso;
import com.libreria.negocio.casouso.libro.impl.ActualizarLibroCasoUsoImpl;
import com.libreria.negocio.dominio.CategoriaDominio;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.negocio.dominio.TipoLibroDominio;
import com.libreria.negocio.fachada.libro.ActualizarLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarLibroFachadaImpl implements ActualizarLibroFachada {

    private final DAOFactory daoFactory;
    private final ActualizarLibroCasoUso casoUso;

    public ActualizarLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarLibroCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final LibroDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            final LibroDominio dominio = new LibroDominio.Builder()
                    .id(datos.getId())
                    .titulo(datos.getTitulo())
                    .tipoLibro(new TipoLibroDominio.Builder()
                            .id(datos.getTipoLibro().getId())
                            .build())
                    .categoria(new CategoriaDominio.Builder()
                            .id(datos.getCategoria().getId())
                            .build())
                    .editorial(new EditorialDominio.Builder()
                            .id(datos.getEditorial().getId())
                            .build())
                    .disponibles(datos.getDisponibles())
                    .build();

            casoUso.ejecutar(dominio);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el libro.", "Error técnico inesperado en ActualizarLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
