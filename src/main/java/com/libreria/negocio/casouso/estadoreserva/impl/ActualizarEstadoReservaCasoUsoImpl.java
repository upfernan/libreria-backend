package com.libreria.negocio.casouso.estadoreserva.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.negocio.casouso.estadoreserva.ActualizarEstadoReservaCasoUso;
import com.libreria.negocio.dominio.EstadoReservaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEstadoReservaCasoUsoImpl implements ActualizarEstadoReservaCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarEstadoReservaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final EstadoReservaDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P4 — Validar que el estado de reserva exista en el sistema
        validarExistencia(datos.getId());
        // P2 — Validar que no exista otro estado de reserva con el mismo nombre
        validarNombreUnicoExcluyendo(datos.getNombre(), datos.getId());
        
        daoFactory.getEstadoReservaDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final EstadoReservaDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del estado de reserva son obligatorios.", "Se recibió un objeto EstadoReservaDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del estado de reserva es obligatorio.", "El campo id llegó nulo en EstadoReservaDominio.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre del estado de reserva es obligatorio.", "El campo nombre llegó nulo o vacío en EstadoReservaDominio.");
        }
        if (UtilTexto.esNula(datos.getDescripcion())) {
            throw GestorLibreriaExcepcion.crear("La descripción del estado de reserva es obligatoria.", "El campo descripcion llegó nulo o vacío en EstadoReservaDominio.");
        }
        if (!UtilTexto.tieneLongitudValida(datos.getDescripcion(), 10, 50)) {
            throw GestorLibreriaExcepcion.crear("La descripción del estado de reserva debe tener entre 10 y 50 caracteres.", "descripcion con longitud inválida en EstadoReservaDominio: " + datos.getDescripcion().length() + " caracteres.");
        }
    }

    // P4 — Validar que el estado de reserva exista en el sistema
    private void validarExistencia(final UUID id) {
        final EstadoReservaEntidad entidad = daoFactory.getEstadoReservaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El estado de reserva indicado no existe en el sistema.", "No se encontró EstadoReserva con id: " + id);
        }
    }

    // P2 — Validar que no exista otro estado de reserva con el mismo nombre excluyendo el actual
    private void validarNombreUnicoExcluyendo(final String nombre, final UUID id) {
        final EstadoReservaEntidad filtro = new EstadoReservaEntidad.Builder().nombre(nombre).build();
        final List<EstadoReservaEntidad> existentes = daoFactory.getEstadoReservaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes)) {
            for (final EstadoReservaEntidad existente : existentes) {
                if (!existente.getId().equals(id)) {
                    throw GestorLibreriaExcepcion.crear("Ya existe otro estado de reserva con ese nombre.", "nombre duplicado en estadoReserva: " + nombre);
                }
            }
        }
    }

    private EstadoReservaEntidad construirEntidad(final EstadoReservaDominio datos) {
        return new EstadoReservaEntidad.Builder()
                .id(datos.getId())
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
    }
}
