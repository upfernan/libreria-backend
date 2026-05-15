package com.libreria.negocio.casouso.categoria.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.negocio.casouso.categoria.ActualizarCategoriaCasoUso;
import com.libreria.negocio.dominio.CategoriaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarCategoriaCasoUsoImpl implements ActualizarCategoriaCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarCategoriaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final CategoriaDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P4 — Validar que la categoría exista en el sistema
        validarExistencia(datos.getId());
        // P2 — Validar que no exista otra categoría con el mismo nombre
        validarNombreUnicoExcluyendo(datos.getNombre(), datos.getId());
        // P1 — Actualizar la categoría en el sistema
        daoFactory.getCategoriaDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final CategoriaDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la categoría son obligatorios.", "Se recibió un objeto CategoriaDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador de la categoría es obligatorio.", "El campo id llegó nulo en CategoriaDominio.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre de la categoría es obligatorio.", "El campo nombre llegó nulo o vacío en CategoriaDominio.");
        }
        if (UtilTexto.esNula(datos.getDescripcion())) {
            throw GestorLibreriaExcepcion.crear("La descripción de la categoría es obligatoria.", "El campo descripcion llegó nulo o vacío en CategoriaDominio.");
        }
    }

    // P4 — Validar que la categoría exista en el sistema
    private void validarExistencia(final UUID id) {
        final CategoriaEntidad entidad = daoFactory.getCategoriaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La categoría indicada no existe en el sistema.", "No se encontró categoría con id: " + id);
        }
    }

    // P2 — Validar que no exista otra categoría con el mismo nombre excluyendo la actual
    private void validarNombreUnicoExcluyendo(final String nombre, final UUID id) {
        final CategoriaEntidad filtro = new CategoriaEntidad.Builder().nombre(nombre).build();
        final List<CategoriaEntidad> existentes = daoFactory.getCategoriaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes)) {
            for (final CategoriaEntidad existente : existentes) {
                if (!existente.getId().equals(id)) {
                    throw GestorLibreriaExcepcion.crear("Ya existe otra categoría con ese nombre.", "nombre duplicado en categoria: " + nombre);
                }
            }
        }
    }

    private CategoriaEntidad construirEntidad(final CategoriaDominio datos) {
        return new CategoriaEntidad.Builder()
                .id(datos.getId())
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
    }
}
