package com.libreria.negocio.casouso.prestamo.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.prestamo.CerrarPrestamoCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class CerrarPrestamoCasoUsoImpl implements CerrarPrestamoCasoUso {

    private final DAOFactory daoFactory;

    public CerrarPrestamoCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador del préstamo es obligatorio.", "Se recibió un UUID nulo para cerrar préstamo.");
        }
        // P3 — Validar que el préstamo exista en el sistema
        final PrestamoEntidad prestamo = validarExistencia(id);
        // P4 — Validar que el préstamo esté en un estado que permita cerrarlo
        validarEstadoCerrable(prestamo);
        // P1 — Cerrar el préstamo actualizando su estado a "finalizado"
        cerrarPrestamo(prestamo);
    }

    // P3 — Validar que el préstamo exista en el sistema
    private PrestamoEntidad validarExistencia(final UUID id) {
        final PrestamoEntidad entidad = daoFactory.getPrestamoDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El préstamo indicado no existe en el sistema.", "No se encontró Préstamo con id: " + id);
        }
        return entidad;
    }

    // P4 — Validar que el préstamo tenga un estado que permita cerrarlo (activo o vencido)
    private void validarEstadoCerrable(final PrestamoEntidad prestamo) {
        final String estadoNombre = prestamo.getEstadoPrestamo().getNombre();
        if (!"activo".equalsIgnoreCase(estadoNombre) && !"vencido".equalsIgnoreCase(estadoNombre)) {
            throw GestorLibreriaExcepcion.crear(
                    "El préstamo no puede cerrarse porque su estado actual es '" + estadoNombre + "'.",
                    "estadoPrestamo inválido para cierre: " + estadoNombre + " — prestamoId: " + prestamo.getId());
        }
    }

    // P1 — Actualizar el estado del préstamo a "finalizado"
    private void cerrarPrestamo(final PrestamoEntidad prestamo) {
        final List<EstadoPrestamoEntidad> estados = daoFactory.getEstadoPrestamoDAO()
                .consultarPorFiltro(new EstadoPrestamoEntidad.Builder().nombre("finalizado").build());
        if (UtilObjeto.esNulo(estados) || estados.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("No se encontró el estado 'finalizado' requerido para cerrar el préstamo.", "EstadoPrestamo 'finalizado' no configurado en el sistema.");
        }
        daoFactory.getPrestamoDAO().actualizar(prestamo.getId(), new PrestamoEntidad.Builder()
                .id(prestamo.getId())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionEsperada(prestamo.getFechaDevolucionEsperada())
                .estadoPrestamo(estados.get(0))
                .reserva(prestamo.getReserva())
                .usuario(prestamo.getUsuario())
                .ejemplar(prestamo.getEjemplar())
                .build());
    }

}
