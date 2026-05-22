package com.libreria.negocio.fachada.autorlibro.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorLibroDTO;
import com.libreria.negocio.assembler.dto.impl.AutorLibroDTOAssembler;
import com.libreria.negocio.casouso.autorlibro.AsociarAutorLibroCasoUso;
import com.libreria.negocio.casouso.autorlibro.impl.AsociarAutorLibroCasoUsoImpl;
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

            final AutorLibroDTO datosEfectivos = UtilObjeto.obtenerValorDefecto(datos, new AutorLibroDTO.Builder().build());
            casoUso.ejecutar(AutorLibroDTOAssembler.getInstance().ensamblarDominio(datosEfectivos));

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
