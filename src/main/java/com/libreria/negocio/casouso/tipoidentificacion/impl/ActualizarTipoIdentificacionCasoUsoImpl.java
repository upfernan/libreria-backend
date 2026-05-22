package com.libreria.negocio.casouso.tipoidentificacion.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.negocio.casouso.tipoidentificacion.ActualizarTipoIdentificacionCasoUso;
import com.libreria.negocio.dominio.TipoIdentificacionDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarTipoIdentificacionCasoUsoImpl implements ActualizarTipoIdentificacionCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarTipoIdentificacionCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final TipoIdentificacionDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P4 — Validar que el tipo de identificación exista en el sistema
        validarExistencia(datos.getId());
        // P2 — Validar que no exista otro tipo de identificación con el mismo nombre
        validarNombreUnicoExcluyendo(datos.getNombre(), datos.getId());
        
        daoFactory.getTipoIdentificacionDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final TipoIdentificacionDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del tipo de identificación son obligatorios.", "Se recibió un objeto TipoIdentificacionDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del tipo de identificación es obligatorio.", "El campo id llegó nulo en TipoIdentificacionDominio.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre del tipo de identificación es obligatorio.", "El campo nombre llegó nulo o vacío en TipoIdentificacionDominio.");
        }
        if (!UtilTexto.soloLetrasMayusculasYEspacios(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre del tipo de identificación solo puede contener letras mayúsculas y espacios.", "nombre con formato inválido en TipoIdentificacionDominio: " + datos.getNombre());
        }
        if (UtilTexto.esNula(datos.getDescripcion())) {
            throw GestorLibreriaExcepcion.crear("La descripción del tipo de identificación es obligatoria.", "El campo descripcion llegó nulo o vacío en TipoIdentificacionDominio.");
        }
    }

    // P4 — Validar que el tipo de identificación exista en el sistema
    private void validarExistencia(final UUID id) {
        final TipoIdentificacionEntidad entidad = daoFactory.getTipoIdentificacionDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El tipo de identificación indicado no existe en el sistema.", "No se encontró TipoIdentificacion con id: " + id);
        }
    }

    // P2 — Validar que no exista otro tipo de identificación con el mismo nombre excluyendo el actual
    private void validarNombreUnicoExcluyendo(final String nombre, final UUID id) {
        final TipoIdentificacionEntidad filtro = new TipoIdentificacionEntidad.Builder().nombre(nombre).build();
        final List<TipoIdentificacionEntidad> existentes = daoFactory.getTipoIdentificacionDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes)) {
            for (final TipoIdentificacionEntidad existente : existentes) {
                if (!existente.getId().equals(id)) {
                    throw GestorLibreriaExcepcion.crear("Ya existe otro tipo de identificación con ese nombre.", "nombre duplicado en tipoIdentificacion: " + nombre);
                }
            }
        }
    }

    private TipoIdentificacionEntidad construirEntidad(final TipoIdentificacionDominio datos) {
        return new TipoIdentificacionEntidad.Builder()
                .id(datos.getId())
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
    }
}
