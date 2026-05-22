package com.libreria.negocio.casouso.reserva.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.casouso.reserva.RetirarReservaCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarReservaCasoUsoImpl implements RetirarReservaCasoUso {

    private final DAOFactory daoFactory;

    public RetirarReservaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P7 — La reserva debe estar registrada en el sistema
        final ReservaEntidad reserva = validarExistencia(id);

        // P11 — La reserva solo puede eliminarse si está en estado cancelada o atendida
        validarEstadoEliminable(reserva);

        
        daoFactory.getReservaDAO().eliminar(reserva.getId());
    }

    // P7 — La reserva debe estar registrada en el sistema
    private ReservaEntidad validarExistencia(final UUID id) {
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador de la reserva es obligatorio.", "Se recibió un UUID nulo para eliminar reserva.");
        }
        final ReservaEntidad reserva = daoFactory.getReservaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(reserva) || UtilObjeto.esNulo(reserva.getId())) {
            throw GestorLibreriaExcepcion.crear("La reserva indicada no existe en el sistema.", "No se encontró Reserva con id: " + id);
        }
        return reserva;
    }

    // P11 — La reserva solo puede eliminarse si está en estado cancelada o atendida
    private void validarEstadoEliminable(final ReservaEntidad reserva) {
        final String estadoNombre = reserva.getEstadoReserva().getNombre();
        if (!"cancelada".equalsIgnoreCase(estadoNombre) && !"atendida".equalsIgnoreCase(estadoNombre)) {
            throw GestorLibreriaExcepcion.crear(
                    "La reserva no puede eliminarse en su estado actual '" + estadoNombre + "'.",
                    "Solo se pueden eliminar reservas en estado cancelada o atendida. reservaId: " + reserva.getId());
        }
    }

}
