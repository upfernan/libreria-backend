package com.libreria.negocio.casouso.tipolibro.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.tipolibro.ActualizarTipoLibroCasoUso;
import com.libreria.negocio.dominio.TipoLibroDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarTipoLibroCasoUsoImpl implements ActualizarTipoLibroCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarTipoLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final TipoLibroDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P4 — Validar que el tipo de libro exista en el sistema
        validarExistencia(datos.getId());
        // P2 — Validar que no exista otro tipo de libro con el mismo nombre
        validarNombreUnicoExcluyendo(datos.getNombre(), datos.getId());
        // P1 — Actualizar el tipo de libro en el sistema
        daoFactory.getTipoLibroDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final TipoLibroDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del tipo de libro son obligatorios.", "Se recibió un objeto TipoLibroDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del tipo de libro es obligatorio.", "El campo id llegó nulo en TipoLibroDominio.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre del tipo de libro es obligatorio.", "El campo nombre llegó nulo o vacío en TipoLibroDominio.");
        }
        if (UtilTexto.esNula(datos.getDescripcion())) {
            throw GestorLibreriaExcepcion.crear("La descripción del tipo de libro es obligatoria.", "El campo descripcion llegó nulo o vacío en TipoLibroDominio.");
        }
    }

    // P4 — Validar que el tipo de libro exista en el sistema
    private void validarExistencia(final UUID id) {
        final TipoLibroEntidad entidad = daoFactory.getTipoLibroDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El tipo de libro indicado no existe en el sistema.", "No se encontró TipoLibro con id: " + id);
        }
    }

    // P2 — Validar que no exista otro tipo de libro con el mismo nombre excluyendo el actual
    private void validarNombreUnicoExcluyendo(final String nombre, final UUID id) {
        final TipoLibroEntidad filtro = new TipoLibroEntidad.Builder().nombre(nombre).build();
        final List<TipoLibroEntidad> existentes = daoFactory.getTipoLibroDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes)) {
            for (final TipoLibroEntidad existente : existentes) {
                if (!existente.getId().equals(id)) {
                    throw GestorLibreriaExcepcion.crear("Ya existe otro tipo de libro con ese nombre.", "nombre duplicado en tipoLibro: " + nombre);
                }
            }
        }
    }

    private TipoLibroEntidad construirEntidad(final TipoLibroDominio datos) {
        return new TipoLibroEntidad.Builder()
                .id(datos.getId())
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
    }
}
