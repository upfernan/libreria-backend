package com.libreria.negocio.fachada.usuario.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.usuario.RetirarUsuarioCasoUso;
import com.libreria.negocio.casouso.usuario.impl.RetirarUsuarioCasoUsoImpl;
import com.libreria.negocio.fachada.usuario.RetirarUsuarioFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarUsuarioFachadaImpl implements RetirarUsuarioFachada {

    private final DAOFactory daoFactory;
    private final RetirarUsuarioCasoUso casoUso;

    public RetirarUsuarioFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RetirarUsuarioCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final UUID id) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(id);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar el usuario.", "Error técnico inesperado en RetirarUsuarioFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
