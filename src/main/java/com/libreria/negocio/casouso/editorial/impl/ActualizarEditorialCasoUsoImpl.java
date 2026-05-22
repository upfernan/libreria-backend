package com.libreria.negocio.casouso.editorial.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.casouso.editorial.ActualizarEditorialCasoUso;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEditorialCasoUsoImpl implements ActualizarEditorialCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarEditorialCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final EditorialDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P4 — Validar que la editorial exista en el sistema
        validarExistencia(datos.getId());
        // P2 — Validar que no exista otra editorial con el mismo nombre
        validarNombreUnicoExcluyendo(datos.getNombre(), datos.getId());
      
        daoFactory.getEditorialDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final EditorialDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la editorial son obligatorios.", "Se recibió un objeto EditorialDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador de la editorial es obligatorio.", "El campo id llegó nulo en EditorialDominio.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre de la editorial es obligatorio.", "El campo nombre llegó nulo o vacío en EditorialDominio.");
        }
        if (!UtilTexto.soloLetrasYEspacios(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre de la editorial solo puede contener letras y espacios.", "nombre con formato inválido en EditorialDominio: " + datos.getNombre());
        }
    }

    // P4 — Validar que la editorial exista en el sistema
    private void validarExistencia(final UUID id) {
        final EditorialEntidad entidad = daoFactory.getEditorialDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La editorial indicada no existe en el sistema.", "No se encontró editorial con id: " + id);
        }
    }

    // P2 — Validar que no exista otra editorial con el mismo nombre excluyendo la actual
    private void validarNombreUnicoExcluyendo(final String nombre, final UUID id) {
        final EditorialEntidad filtro = new EditorialEntidad.Builder().nombre(nombre).build();
        final List<EditorialEntidad> existentes = daoFactory.getEditorialDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes)) {
            for (final EditorialEntidad existente : existentes) {
                if (!existente.getId().equals(id)) {
                    throw GestorLibreriaExcepcion.crear("Ya existe otra editorial con ese nombre.", "nombre duplicado en editorial: " + nombre);
                }
            }
        }
    }

    private EditorialEntidad construirEntidad(final EditorialDominio datos) {
        return new EditorialEntidad.Builder()
                .id(datos.getId())
                .nit(datos.getNit())
                .nombre(datos.getNombre())
                .build();
    }
}
