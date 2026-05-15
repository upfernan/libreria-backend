package com.libreria.negocio.casouso.estadoreserva.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.negocio.casouso.estadoreserva.AgregarEstadoReservaCasoUso;
import com.libreria.negocio.dominio.EstadoReservaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarEstadoReservaCasoUsoImpl implements AgregarEstadoReservaCasoUso {

    private final DAOFactory daoFactory;

    public AgregarEstadoReservaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final EstadoReservaDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P2 — Validar que no exista otro estado de reserva con el mismo nombre
        validarNombreUnico(datos.getNombre());
        // P1 — Registrar el estado de reserva garantizando identificador único
        guardarNuevoEstadoReserva(datos);
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final EstadoReservaDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del estado de reserva son obligatorios.", "Se recibió un objeto EstadoReservaDominio nulo.");
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

    // P2 — Validar que no exista otro estado de reserva con el mismo nombre
    private void validarNombreUnico(final String nombre) {
        final EstadoReservaEntidad filtro = new EstadoReservaEntidad.Builder().nombre(nombre).build();
        final List<EstadoReservaEntidad> existentes = daoFactory.getEstadoReservaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("Ya existe un estado de reserva con ese nombre.", "nombre duplicado en estadoReserva: " + nombre);
        }
    }

    // P1 — Generar id único y persistir el nuevo estado de reserva
    private void guardarNuevoEstadoReserva(final EstadoReservaDominio datos) {
        UUID id = UtilUUID.generar();
        while (!UtilObjeto.esNulo(daoFactory.getEstadoReservaDAO().consultarPorId(id))) {
            id = UtilUUID.generar();
        }
        final EstadoReservaEntidad nueva = new EstadoReservaEntidad.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        daoFactory.getEstadoReservaDAO().crear(nueva);
    }
}
