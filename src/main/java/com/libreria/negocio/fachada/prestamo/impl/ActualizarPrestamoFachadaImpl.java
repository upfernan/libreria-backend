package com.libreria.negocio.fachada.prestamo.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.PrestamoDTO;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.assembler.dto.impl.PrestamoDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.PrestamoEntidadAssembler;
import com.libreria.negocio.casouso.prestamo.ActualizarPrestamoCasoUso;
import com.libreria.negocio.casouso.prestamo.impl.ActualizarPrestamoCasoUsoImpl;
import com.libreria.negocio.fachada.prestamo.ActualizarPrestamoFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarPrestamoFachadaImpl implements ActualizarPrestamoFachada {

    private final DAOFactory daoFactory;
    private final ActualizarPrestamoCasoUso casoUso;

    public ActualizarPrestamoFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarPrestamoCasoUsoImpl(daoFactory);
    }

    @Override
    public PrestamoDTO ejecutar(final PrestamoDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            casoUso.ejecutar(PrestamoDTOAssembler.getInstance().ensamblarDominio(datos));

            daoFactory.confirmarTransaccion();

            final PrestamoEntidad actualizado = daoFactory.getPrestamoDAO().consultarPorId(datos.getId());
            return PrestamoDTOAssembler.getInstance().ensamblarDTO(
                    PrestamoEntidadAssembler.getInstance().ensamblarDominio(
                            UtilObjeto.obtenerValorDefecto(actualizado, new PrestamoEntidad.Builder().build())));

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el préstamo.", "Error técnico inesperado en ActualizarPrestamoFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
