package com.libreria.negocio.fachada.ejemplar.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EjemplarDTO;
import com.libreria.negocio.assembler.dto.impl.EjemplarDTOAssembler;
import com.libreria.negocio.casouso.ejemplar.RegistrarEjemplarCasoUso;
import com.libreria.negocio.casouso.ejemplar.impl.RegistrarEjemplarCasoUsoImpl;
import com.libreria.negocio.fachada.ejemplar.RegistrarEjemplarFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarEjemplarFachadaImpl implements RegistrarEjemplarFachada {

    private DAOFactory daoFactory;
    private RegistrarEjemplarCasoUso casoUso;

    public RegistrarEjemplarFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RegistrarEjemplarCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final EjemplarDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(EjemplarDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar el ejemplar.", "Error técnico inesperado en RegistrarEjemplarFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
