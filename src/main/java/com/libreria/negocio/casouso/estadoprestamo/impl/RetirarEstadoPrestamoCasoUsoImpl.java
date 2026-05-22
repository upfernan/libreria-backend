package com.libreria.negocio.casouso.estadoprestamo.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.estadoprestamo.RetirarEstadoPrestamoCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEstadoPrestamoCasoUsoImpl implements RetirarEstadoPrestamoCasoUso {

    private final DAOFactory daoFactory;

    public RetirarEstadoPrestamoCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P4 — Validar que el estado de préstamo exista en el sistema
        validarExistencia(id);
        // P5 — Validar que el estado de préstamo no esté en uso
        validarNoEnUso(id);
        
        daoFactory.getEstadoPrestamoDAO().eliminar(id);
    }

    // P4 — Validar que el estado de préstamo exista en el sistema
    private void validarExistencia(final UUID id) {
        final EstadoPrestamoEntidad entidad = daoFactory.getEstadoPrestamoDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El estado de préstamo indicado no existe en el sistema.", "No se encontró EstadoPrestamo con id: " + id);
        }
    }

    // P5 — Validar que el estado de préstamo no esté siendo utilizado por algún préstamo
    private void validarNoEnUso(final UUID id) {
        final PrestamoEntidad filtroPrestamo = new PrestamoEntidad.Builder()
                .estadoPrestamo(new EstadoPrestamoEntidad.Builder().id(id).build()).build();
        final List<PrestamoEntidad> prestamos = daoFactory.getPrestamoDAO().consultarPorFiltro(filtroPrestamo);
        if (!UtilObjeto.esNulo(prestamos) && !prestamos.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El estado de préstamo está en uso y no puede eliminarse.", "estadoPrestamoId: " + id);
        }
    }
}
