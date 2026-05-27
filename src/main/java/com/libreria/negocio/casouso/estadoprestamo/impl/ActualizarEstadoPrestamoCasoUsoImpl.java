package com.libreria.negocio.casouso.estadoprestamo.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.negocio.casouso.estadoprestamo.ActualizarEstadoPrestamoCasoUso;
import com.libreria.negocio.dominio.EstadoPrestamoDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEstadoPrestamoCasoUsoImpl implements ActualizarEstadoPrestamoCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarEstadoPrestamoCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final EstadoPrestamoDominio datos) {
        // P3 Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P4 Validar que el estado de préstamo exista en el sistema
        validarExistencia(datos.getId());
        // P2 Validar que no exista otro estado de préstamo con el mismo nombre
        validarNombreUnicoExcluyendo(datos.getNombre(), datos.getId());
        
        daoFactory.getEstadoPrestamoDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P3  Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final EstadoPrestamoDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del estado de préstamo son obligatorios.", "Se recibió un objeto EstadoPrestamoDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del estado de préstamo es obligatorio.", "El campo id llegó nulo en EstadoPrestamoDominio.");
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

    // P4  Validar que el estado de préstamo exista en el sistema
    private void validarExistencia(final UUID id) {
        final EstadoPrestamoEntidad entidad = daoFactory.getEstadoPrestamoDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El estado de préstamo indicado no existe en el sistema.", "No se encontró EstadoPrestamo con id: " + id);
        }
    }

    // P2  Validar que no exista otro estado de préstamo con el mismo nombre excluyendo el actual
    private void validarNombreUnicoExcluyendo(final String nombre, final UUID id) {
        final EstadoPrestamoEntidad filtro = new EstadoPrestamoEntidad.Builder().nombre(nombre).build();
        final List<EstadoPrestamoEntidad> existentes = daoFactory.getEstadoPrestamoDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes)) {
            for (final EstadoPrestamoEntidad existente : existentes) {
                if (!existente.getId().equals(id)) {
                    throw GestorLibreriaExcepcion.crear("Ya existe otro estado de préstamo con ese nombre.", "nombre duplicado en estadoPrestamo: " + nombre);
                }
            }
        }
    }

    private EstadoPrestamoEntidad construirEntidad(final EstadoPrestamoDominio datos) {
        return new EstadoPrestamoEntidad.Builder()
                .id(datos.getId())
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
    }
}
