package com.libreria.negocio.casouso.estadoprestamo.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.negocio.casouso.estadoprestamo.AgregarEstadoPrestamoCasoUso;
import com.libreria.negocio.dominio.EstadoPrestamoDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarEstadoPrestamoCasoUsoImpl implements AgregarEstadoPrestamoCasoUso {

    private final DAOFactory daoFactory;

    public AgregarEstadoPrestamoCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final EstadoPrestamoDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P2 — Validar que no exista otro estado de préstamo con el mismo nombre
        validarNombreUnico(datos.getNombre());
        // P1 — Registrar el estado de préstamo garantizando identificador único
        guardarNuevoEstadoPrestamo(datos);
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final EstadoPrestamoDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del estado de préstamo son obligatorios.", "Se recibió un objeto EstadoPrestamoDominio nulo.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre del estado de préstamo es obligatorio.", "El campo nombre llegó nulo o vacío en EstadoPrestamoDominio.");
        }
        if (UtilTexto.esNula(datos.getDescripcion())) {
            throw GestorLibreriaExcepcion.crear("La descripción del estado de préstamo es obligatoria.", "El campo descripcion llegó nulo o vacío en EstadoPrestamoDominio.");
        }
        if (!UtilTexto.tieneLongitudValida(datos.getDescripcion(), 10, 50)) {
            throw GestorLibreriaExcepcion.crear("La descripción del estado de préstamo debe tener entre 10 y 50 caracteres.", "descripcion con longitud inválida en EstadoPrestamoDominio: " + datos.getDescripcion().length() + " caracteres.");
        }
    }

    // P2 — Validar que no exista otro estado de préstamo con el mismo nombre
    private void validarNombreUnico(final String nombre) {
        final EstadoPrestamoEntidad filtro = new EstadoPrestamoEntidad.Builder().nombre(nombre).build();
        final List<EstadoPrestamoEntidad> existentes = daoFactory.getEstadoPrestamoDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("Ya existe un estado de préstamo con ese nombre.", "nombre duplicado en estadoPrestamo: " + nombre);
        }
    }

    // P1 — Generar id único y persistir el nuevo estado de préstamo
    private void guardarNuevoEstadoPrestamo(final EstadoPrestamoDominio datos) {
        UUID id = UtilUUID.generar();
        while (UtilUUID.tieneValor(daoFactory.getEstadoPrestamoDAO().consultarPorId(id).getId())) {
            id = UtilUUID.generar();
        }
        final EstadoPrestamoEntidad nueva = new EstadoPrestamoEntidad.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        daoFactory.getEstadoPrestamoDAO().crear(nueva);
    }
}
