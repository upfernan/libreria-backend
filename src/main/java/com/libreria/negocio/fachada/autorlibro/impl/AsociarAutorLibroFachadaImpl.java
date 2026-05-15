package com.libreria.negocio.fachada.autorlibro.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.dto.AutorLibroDTO;
import com.libreria.dto.LibroDTO;
import com.libreria.negocio.casouso.autorlibro.AsociarAutorLibroCasoUso;
import com.libreria.negocio.casouso.autorlibro.impl.AsociarAutorLibroCasoUsoImpl;
import com.libreria.negocio.dominio.AutorDominio;
import com.libreria.negocio.dominio.AutorLibroDominio;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.negocio.fachada.autorlibro.AsociarAutorLibroFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AsociarAutorLibroFachadaImpl implements AsociarAutorLibroFachada {

    private final DAOFactory daoFactory;
    private final AsociarAutorLibroCasoUso casoUso;

    public AsociarAutorLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new AsociarAutorLibroCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final AutorLibroDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            final AutorDTO autorEfectivo = UtilObjeto.obtenerValorDefecto(datos.getAutor(), new AutorDTO.Builder().build());
            final LibroDTO libroEfectivo = UtilObjeto.obtenerValorDefecto(datos.getLibro(), new LibroDTO.Builder().build());

            final AutorLibroDominio dominio = new AutorLibroDominio.Builder()
                    .autor(new AutorDominio.Builder().id(autorEfectivo.getId()).build())
                    .libro(new LibroDominio.Builder().id(libroEfectivo.getId()).build())
                    .build();

            casoUso.ejecutar(dominio);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al asociar el autor al libro.", "Error técnico inesperado en AsociarAutorLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
