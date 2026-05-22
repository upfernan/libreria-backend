package com.libreria.negocio.fachada.autor.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.negocio.assembler.dto.impl.AutorDTOAssembler;
import com.libreria.negocio.casouso.autor.AgregarAutorCasoUso;
import com.libreria.negocio.casouso.autor.impl.AgregarAutorCasoUsoImpl;
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

            casoUso.ejecutar(AutorDTOAssembler.getInstance().ensamblarDominio(datos));

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
