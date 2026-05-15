package com.libreria.negocio.casouso.estadoreserva.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.casouso.estadoreserva.RetirarEstadoReservaCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEstadoReservaCasoUsoImpl implements RetirarEstadoReservaCasoUso {

    private final DAOFactory daoFactory;

    public RetirarEstadoReservaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P4 — Validar que el estado de reserva exista en el sistema
        validarExistencia(id);
        // P5 — Validar que el estado de reserva no esté en uso
        validarNoEnUso(id);
        // P1 — Eliminar el estado de reserva del sistema
        daoFactory.getEstadoReservaDAO().eliminar(id);
    }

    // P4 — Validar que el estado de reserva exista en el sistema
    private void validarExistencia(final UUID id) {
        final EstadoReservaEntidad entidad = daoFactory.getEstadoReservaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El estado de reserva indicado no existe en el sistema.", "No se encontró EstadoReserva con id: " + id);
        }
    }

    // P5 — Validar que el estado de reserva no esté siendo utilizado por alguna reserva
    private void validarNoEnUso(final UUID id) {
        final ReservaEntidad filtroReserva = new ReservaEntidad.Builder()
                .estadoReserva(new EstadoReservaEntidad.Builder().id(id).build()).build();
        final List<ReservaEntidad> reservas = daoFactory.getReservaDAO().consultarPorFiltro(filtroReserva);
        if (!UtilObjeto.esNulo(reservas) && !reservas.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El estado de reserva está en uso y no puede eliminarse.", "estadoReservaId: " + id);
        }
    }
}
