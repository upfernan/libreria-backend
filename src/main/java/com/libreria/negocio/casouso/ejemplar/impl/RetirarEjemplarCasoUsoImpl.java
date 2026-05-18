package com.libreria.negocio.casouso.ejemplar.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.ejemplar.RetirarEjemplarCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEjemplarCasoUsoImpl implements RetirarEjemplarCasoUso {

    private final DAOFactory daoFactory;

    public RetirarEjemplarCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador del ejemplar es obligatorio.", "Se recibió un UUID nulo para retirar ejemplar.");
        }
        // P3 — Validar que el ejemplar exista en el sistema
        validarExistencia(id);
        // P4 — Validar que el ejemplar no tenga préstamos activos
        validarNoEnUso(id);
        // P1 — Eliminar el ejemplar del sistema
        daoFactory.getEjemplarDAO().eliminar(id);
    }

    // P3 — Validar que el ejemplar exista en el sistema
    private void validarExistencia(final UUID id) {
        final EjemplarEntidad entidad = daoFactory.getEjemplarDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El ejemplar indicado no existe en el sistema.", "No se encontró Ejemplar con id: " + id);
        }
    }

    // P4 — Validar que el ejemplar no tenga préstamos activos
    private void validarNoEnUso(final UUID id) {
        final PrestamoEntidad filtro = new PrestamoEntidad.Builder()
                .ejemplar(new EjemplarEntidad.Builder().id(id).build())
                .estadoPrestamo(new EstadoPrestamoEntidad.Builder().nombre("activo").build())
                .build();
        final List<PrestamoEntidad> prestamosActivos = daoFactory.getPrestamoDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(prestamosActivos) && !prestamosActivos.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El ejemplar tiene préstamos activos y no puede eliminarse.", "ejemplarId: " + id);
        }
    }

}
