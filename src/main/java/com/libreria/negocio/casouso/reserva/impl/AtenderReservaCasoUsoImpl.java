package com.libreria.negocio.casouso.reserva.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.casouso.reserva.AtenderReservaCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AtenderReservaCasoUsoImpl implements AtenderReservaCasoUso {

    private final DAOFactory daoFactory;

    public AtenderReservaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador de la reserva es obligatorio.", "Se recibió un UUID nulo para atender reserva.");
        }
        // P3 — Validar que la reserva exista en el sistema
        final ReservaEntidad reserva = validarExistencia(id);
        // P4 — Validar que la reserva esté en estado "pendiente"
        validarEstadoAtendible(reserva);
        // P1 — Marcar la reserva como "atendida"
        atenderReserva(reserva);
    }

    // P3 — Validar que la reserva exista en el sistema
    private ReservaEntidad validarExistencia(final UUID id) {
        final ReservaEntidad entidad = daoFactory.getReservaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La reserva indicada no existe en el sistema.", "No se encontró Reserva con id: " + id);
        }
        return entidad;
    }

    // P4 — Validar que la reserva esté en un estado que permita atenderla
    private void validarEstadoAtendible(final ReservaEntidad reserva) {
        final String estadoNombre = reserva.getEstadoReserva().getNombre();
        if (!"pendiente".equalsIgnoreCase(estadoNombre)) {
            throw GestorLibreriaExcepcion.crear(
                    "La reserva no puede atenderse porque su estado actual es '" + estadoNombre + "'.",
                    "estadoReserva inválido para atención: " + estadoNombre + " — reservaId: " + reserva.getId());
        }
    }

    // P1 — Actualizar el estado de la reserva a "atendida"
    private void atenderReserva(final ReservaEntidad reserva) {
        final List<EstadoReservaEntidad> estados = daoFactory.getEstadoReservaDAO()
                .consultarPorFiltro(new EstadoReservaEntidad.Builder().nombre("atendida").build());
        if (UtilObjeto.esNulo(estados) || estados.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("No se encontró el estado 'atendida' requerido para atender la reserva.", "EstadoReserva 'atendida' no configurado en el sistema.");
        }
        daoFactory.getReservaDAO().actualizar(reserva.getId(), new ReservaEntidad.Builder()
                .id(reserva.getId())
                .fechaReserva(reserva.getFechaReserva())
                .fechaExpiracion(reserva.getFechaExpiracion())
                .estadoReserva(estados.get(0))
                .usuario(reserva.getUsuario())
                .libro(reserva.getLibro())
                .build());
    }

}
