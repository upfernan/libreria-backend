package com.libreria.negocio.fachada.usuario.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.UsuarioDTO;
import com.libreria.negocio.assembler.dto.impl.UsuarioDTOAssembler;
import com.libreria.negocio.casouso.usuario.ActualizarUsuarioCasoUso;
import com.libreria.negocio.casouso.usuario.impl.ActualizarUsuarioCasoUsoImpl;
import com.libreria.negocio.fachada.usuario.ActualizarUsuarioFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarUsuarioFachadaImpl implements ActualizarUsuarioFachada {

    private final DAOFactory daoFactory;
    private final ActualizarUsuarioCasoUso casoUso;

    public ActualizarUsuarioFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarUsuarioCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final UsuarioDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(UsuarioDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el usuario.", "Error técnico inesperado en ActualizarUsuarioFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
