package com.libreria.negocio.fachada.autor.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.negocio.casouso.autor.ActualizarAutorCasoUso;
import com.libreria.negocio.casouso.autor.impl.ActualizarAutorCasoUsoImpl;
import com.libreria.negocio.dominio.AutorDominio;
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

            final AutorDominio dominio = new AutorDominio.Builder()
                    .id(datos.getId())
                    .primerNombre(datos.getPrimerNombre())
                    .segundoNombre(datos.getSegundoNombre())
                    .primerApellido(datos.getPrimerApellido())
                    .segundoApellido(datos.getSegundoApellido())
                    .build();

            casoUso.ejecutar(dominio);

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
