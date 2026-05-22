package com.libreria.negocio.casouso.prestamo.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.prestamo.ActualizarPrestamoCasoUso;
import com.libreria.negocio.dominio.PrestamoDominio;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarPrestamoCasoUsoImpl implements ActualizarPrestamoCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarPrestamoCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final PrestamoDominio datos) {
        // P11 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
        validarDatosObligatorios(datos);
        // P9 — El préstamo debe estar registrado en el sistema
        final PrestamoEntidad prestamo = validarExistencia(datos.getId());
        // P12 — El préstamo no puede actualizarse si está en estado devuelto o cerrado
        validarNoActivo(prestamo);
        // P13 — La fecha de devolución esperada debe ser posterior a la fecha del préstamo
        validarFechaDevolucion(datos, prestamo);
        // P14 — Si el nuevo estado es activo, el ejemplar no puede tener otro préstamo activo y el préstamo no puede tener una devolución registrada
        validarEjemplarDisponibleSiActivo(datos, prestamo);
        // Actualizar el préstamo en el sistema
        actualizarPrestamo(datos, prestamo);
    }

    // P14 — Si el nuevo estado es activo, el ejemplar no puede tener otro préstamo activo ni el préstamo puede tener una devolución registrada
    private void validarEjemplarDisponibleSiActivo(final PrestamoDominio datos, final PrestamoEntidad prestamo) {
        final List<EstadoPrestamoEntidad> estadosNuevos = daoFactory.getEstadoPrestamoDAO()
                .consultarPorFiltro(new EstadoPrestamoEntidad.Builder().id(datos.getEstadoPrestamo().getId()).build());
        if (UtilObjeto.esNulo(estadosNuevos) || estadosNuevos.isEmpty()) {
            return;
        }
        if (!"activo".equalsIgnoreCase(estadosNuevos.get(0).getNombre())) {
            return;
        }
        // Verificar que el préstamo no tenga ya una devolución registrada
        final List<com.libreria.entidad.DevolucionEntidad> devoluciones = daoFactory.getDevolucionDAO()
                .consultarPorFiltro(new com.libreria.entidad.DevolucionEntidad.Builder()
                        .prestamo(new PrestamoEntidad.Builder().id(prestamo.getId()).build())
                        .build());
        if (!UtilObjeto.esNulo(devoluciones) && !devoluciones.isEmpty()) {
            throw GestorLibreriaExcepcion.crear(
                    "El préstamo no puede volver a estado activo porque ya tiene una devolución registrada.",
                    "prestamoId: " + prestamo.getId());
        }
        // Verificar que el ejemplar no tenga otro préstamo activo
        final List<PrestamoEntidad> prestamosActivos = daoFactory.getPrestamoDAO()
                .consultarPorFiltro(new PrestamoEntidad.Builder()
                        .ejemplar(new EjemplarEntidad.Builder().id(prestamo.getEjemplar().getId()).build())
                        .estadoPrestamo(new EstadoPrestamoEntidad.Builder().nombre("activo").build())
                        .build());
        final boolean otroActivo = !UtilObjeto.esNulo(prestamosActivos) && prestamosActivos.stream()
                .anyMatch(p -> !p.getId().equals(prestamo.getId()));
        if (otroActivo) {
            throw GestorLibreriaExcepcion.crear(
                    "El ejemplar ya tiene otro préstamo activo y no puede activarse nuevamente.",
                    "ejemplarId: " + prestamo.getEjemplar().getId() + ", prestamoId: " + prestamo.getId());
        }
    }

    // P11 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
    private void validarDatosObligatorios(final PrestamoDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del préstamo son obligatorios.", "Se recibió un objeto PrestamoDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId()) || datos.getId().equals(UtilUUID.UUID_DEFECTO)) {
            throw GestorLibreriaExcepcion.crear("El identificador del préstamo es obligatorio.", "El campo id llegó nulo o vacío en PrestamoDominio.");
        }
        if (UtilObjeto.esNulo(datos.getEstadoPrestamo()) || UtilUUID.esNulo(datos.getEstadoPrestamo().getId())) {
            throw GestorLibreriaExcepcion.crear("El estado del préstamo es obligatorio.", "El campo estadoPrestamo llegó nulo o vacío en PrestamoDominio.");
        }
        if (UtilObjeto.esNulo(datos.getFechaDevolucionEsperada()) || datos.getFechaDevolucionEsperada().equals(UtilFecha.FECHA_DEFECTO)) {
            throw GestorLibreriaExcepcion.crear("La fecha de devolución esperada es obligatoria.", "El campo fechaDevolucionEsperada llegó nulo o con valor por defecto en PrestamoDominio.");
        }
    }

    // P9 — El préstamo debe estar registrado en el sistema
    private PrestamoEntidad validarExistencia(final java.util.UUID id) {
        final PrestamoEntidad entidad = daoFactory.getPrestamoDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El préstamo indicado no existe en el sistema.", "No se encontró Préstamo con id: " + id);
        }
        return entidad;
    }

    // P12 — El préstamo no puede actualizarse si está en estado devuelto o cerrado
    private void validarNoActivo(final PrestamoEntidad prestamo) {
        final String estado = prestamo.getEstadoPrestamo().getNombre();
        if ("devuelto".equalsIgnoreCase(estado) || "cerrado".equalsIgnoreCase(estado)) {
            throw GestorLibreriaExcepcion.crear(
                    "El préstamo no puede actualizarse porque ya fue " + estado + ".",
                    "prestamoId: " + prestamo.getId() + ", estado: " + estado);
        }
    }

    // P13 — La fecha de devolución esperada debe ser posterior a la fecha del préstamo
    private void validarFechaDevolucion(final PrestamoDominio datos, final PrestamoEntidad prestamo) {
        if (!datos.getFechaDevolucionEsperada().isAfter(prestamo.getFechaPrestamo())) {
            throw GestorLibreriaExcepcion.crear(
                    "La fecha de devolución esperada debe ser posterior a la fecha del préstamo.",
                    "fechaDevolucionEsperada: " + datos.getFechaDevolucionEsperada() + ", fechaPrestamo: " + prestamo.getFechaPrestamo());
        }
    }

    // Actualizar el préstamo en el sistema
    private void actualizarPrestamo(final PrestamoDominio datos, final PrestamoEntidad prestamo) {
        final List<EstadoPrestamoEntidad> estados = daoFactory.getEstadoPrestamoDAO()
                .consultarPorFiltro(new EstadoPrestamoEntidad.Builder().id(datos.getEstadoPrestamo().getId()).build());
        if (UtilObjeto.esNulo(estados) || estados.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El estado del préstamo indicado no existe en el sistema.", "estadoPrestamoId: " + datos.getEstadoPrestamo().getId());
        }
        daoFactory.getPrestamoDAO().actualizar(prestamo.getId(), new PrestamoEntidad.Builder()
                .id(prestamo.getId())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionEsperada(datos.getFechaDevolucionEsperada())
                .estadoPrestamo(estados.get(0))
                .reserva(prestamo.getReserva())
                .usuario(prestamo.getUsuario())
                .ejemplar(prestamo.getEjemplar())
                .build());
    }

}
