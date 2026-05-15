package com.libreria.negocio.casouso.reserva.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.casouso.reserva.CancelarReservaCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class CancelarReservaCasoUsoImpl implements CancelarReservaCasoUso {

    private final DAOFactory daoFactory;

    public CancelarReservaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador de la reserva es obligatorio.", "Se recibió un UUID nulo para cancelar reserva.");
        }

        // Validar que la reserva exista en el sistema
        final ReservaEntidad reserva = daoFactory.getReservaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(reserva) || UtilObjeto.esNulo(reserva.getId())) {
            throw GestorLibreriaExcepcion.crear("La reserva indicada no existe en el sistema.", "No se encontró Reserva con id: " + id);
        }

        // Validar que la reserva esté en un estado cancelable (pendiente o asignada)
        final String estadoNombre = reserva.getEstadoReserva().getNombre();
        if (!"pendiente".equalsIgnoreCase(estadoNombre) && !"asignada".equalsIgnoreCase(estadoNombre)) {
            throw GestorLibreriaExcepcion.crear(
                    "La reserva no puede cancelarse porque su estado actual es '" + estadoNombre + "'.",
                    "estadoReserva inválido para cancelación: " + estadoNombre + " — reservaId: " + id);
        }

        // Obtener el estado "cancelada" del sistema
        final List<EstadoReservaEntidad> estadosCancelada = daoFactory.getEstadoReservaDAO()
                .consultarPorFiltro(new EstadoReservaEntidad.Builder().nombre("cancelada").build());
        if (UtilObjeto.esNulo(estadosCancelada) || estadosCancelada.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("No se encontró el estado 'cancelada' requerido para cancelar la reserva.", "EstadoReserva 'cancelada' no configurado en el sistema.");
        }

        // Actualizar el estado de la reserva a "cancelada"
        daoFactory.getReservaDAO().actualizar(reserva.getId(), new ReservaEntidad.Builder()
                .id(reserva.getId())
                .fechaReserva(reserva.getFechaReserva())
                .fechaExpiracion(reserva.getFechaExpiracion())
                .estadoReserva(estadosCancelada.get(0))
                .usuario(reserva.getUsuario())
                .libro(reserva.getLibro())
                .build());
    }

}
