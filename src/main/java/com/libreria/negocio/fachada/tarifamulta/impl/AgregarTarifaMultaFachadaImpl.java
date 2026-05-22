package com.libreria.negocio.fachada.tarifamulta.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.negocio.assembler.dto.impl.TarifaMultaDTOAssembler;
import com.libreria.negocio.casouso.tarifamulta.AgregarTarifaMultaCasoUso;
import com.libreria.negocio.casouso.tarifamulta.impl.AgregarTarifaMultaCasoUsoImpl;
import com.libreria.negocio.fachada.tarifamulta.AgregarTarifaMultaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarTarifaMultaFachadaImpl implements AgregarTarifaMultaFachada {

    private final DAOFactory daoFactory;
    private final AgregarTarifaMultaCasoUso casoUso;

    public AgregarTarifaMultaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new AgregarTarifaMultaCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final TarifaMultaDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(TarifaMultaDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar la tarifa de multa.", "Error técnico inesperado en AgregarTarifaMultaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
