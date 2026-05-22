package com.libreria.negocio.fachada.signatura.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.SignaturaDTO;
import com.libreria.negocio.assembler.dto.impl.SignaturaDTOAssembler;
import com.libreria.negocio.casouso.signatura.ActualizarSignaturaCasoUso;
import com.libreria.negocio.casouso.signatura.impl.ActualizarSignaturaCasoUsoImpl;
import com.libreria.negocio.fachada.signatura.ActualizarSignaturaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarSignaturaFachadaImpl implements ActualizarSignaturaFachada {

    private final DAOFactory daoFactory;
    private final ActualizarSignaturaCasoUso casoUso;

    public ActualizarSignaturaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarSignaturaCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final SignaturaDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(SignaturaDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar la signatura.", "Error técnico inesperado en ActualizarSignaturaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
