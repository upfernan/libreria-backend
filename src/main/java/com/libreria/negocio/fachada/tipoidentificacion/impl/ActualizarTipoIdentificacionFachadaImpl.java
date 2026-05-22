package com.libreria.negocio.fachada.tipoidentificacion.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoIdentificacionDTO;
import com.libreria.negocio.assembler.dto.impl.TipoIdentificacionDTOAssembler;
import com.libreria.negocio.casouso.tipoidentificacion.ActualizarTipoIdentificacionCasoUso;
import com.libreria.negocio.casouso.tipoidentificacion.impl.ActualizarTipoIdentificacionCasoUsoImpl;
import com.libreria.negocio.fachada.tipoidentificacion.ActualizarTipoIdentificacionFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarTipoIdentificacionFachadaImpl implements ActualizarTipoIdentificacionFachada {

    private final DAOFactory daoFactory;
    private final ActualizarTipoIdentificacionCasoUso casoUso;

    public ActualizarTipoIdentificacionFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarTipoIdentificacionCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final TipoIdentificacionDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(TipoIdentificacionDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el tipo de identificación.", "Error técnico inesperado en ActualizarTipoIdentificacionFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
