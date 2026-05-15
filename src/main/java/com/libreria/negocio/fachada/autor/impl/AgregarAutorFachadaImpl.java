package com.libreria.negocio.fachada.autor.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.negocio.casouso.autor.AgregarAutorCasoUso;
import com.libreria.negocio.casouso.autor.impl.AgregarAutorCasoUsoImpl;
import com.libreria.negocio.dominio.AutorDominio;
import com.libreria.negocio.fachada.autor.AgregarAutorFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarAutorFachadaImpl implements AgregarAutorFachada {

    private final DAOFactory daoFactory;
    private final AgregarAutorCasoUso casoUso;

    public AgregarAutorFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new AgregarAutorCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final AutorDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            final AutorDominio dominio = new AutorDominio.Builder()
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
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar el autor.", "Error técnico inesperado en AgregarAutorFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
