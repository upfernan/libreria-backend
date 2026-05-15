package com.libreria.negocio.fachada.signatura.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.SignaturaDTO;
import com.libreria.negocio.casouso.signatura.AgregarSignaturaCasoUso;
import com.libreria.negocio.casouso.signatura.impl.AgregarSignaturaCasoUsoImpl;
import com.libreria.negocio.dominio.SignaturaDominio;
import com.libreria.negocio.fachada.signatura.AgregarSignaturaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarSignaturaFachadaImpl implements AgregarSignaturaFachada {

    private final DAOFactory daoFactory;
    private final AgregarSignaturaCasoUso casoUso;

    public AgregarSignaturaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new AgregarSignaturaCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final SignaturaDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            final SignaturaDominio dominio = new SignaturaDominio.Builder()
                    .pasillo(datos.getPasillo())
                    .estante(datos.getEstante())
                    .posicion(datos.getPosicion())
                    .build();

            casoUso.ejecutar(dominio);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar la signatura.", "Error técnico inesperado en AgregarSignaturaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
