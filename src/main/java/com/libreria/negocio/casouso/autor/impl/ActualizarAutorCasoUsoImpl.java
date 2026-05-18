package com.libreria.negocio.casouso.autor.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.casouso.autor.ActualizarAutorCasoUso;
import com.libreria.negocio.dominio.AutorDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarAutorCasoUsoImpl implements ActualizarAutorCasoUso {

    private static final String SUFIJO_CARACTERES = " caracteres.";

    private final DAOFactory daoFactory;

    public ActualizarAutorCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final AutorDominio datos) {
        // P2 — Validar tipo de dato, obligatoriedad, longitud y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P3 — Validar que el autor exista en el sistema
        validarExistencia(datos.getId());
        // P1 — Actualizar el autor en el sistema
        daoFactory.getAutorDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P2 — Datos requeridos válidos en tipo, obligatoriedad y longitud
    private void validarDatosObligatorios(final AutorDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del autor son obligatorios.", "Se recibió un objeto AutorDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del autor es obligatorio.", "El campo id llegó nulo en AutorDominio.");
        }
        if (UtilTexto.esNula(datos.getPrimerNombre())) {
            throw GestorLibreriaExcepcion.crear("El primer nombre del autor es obligatorio.", "El campo primerNombre llegó nulo en AutorDominio.");
        }
        if (!UtilTexto.tieneLongitudValida(datos.getPrimerNombre(), 1, 20)) {
            throw GestorLibreriaExcepcion.crear("El primer nombre del autor debe tener entre 1 y 20 caracteres.", "primerNombre con longitud inválida en AutorDominio: " + datos.getPrimerNombre().length() + SUFIJO_CARACTERES);
        }
        if (UtilObjeto.esNulo(datos.getSegundoNombre())) {
            throw GestorLibreriaExcepcion.crear("El segundo nombre del autor es obligatorio.", "El campo segundoNombre llegó nulo en AutorDominio.");
        }
        if (!datos.getSegundoNombre().isEmpty() && !UtilTexto.tieneLongitudValida(datos.getSegundoNombre(), 1, 20)) {
            throw GestorLibreriaExcepcion.crear("El segundo nombre del autor debe tener entre 1 y 20 caracteres si se indica.", "segundoNombre con longitud inválida en AutorDominio: " + datos.getSegundoNombre().length() + SUFIJO_CARACTERES);
        }
        if (UtilTexto.esNula(datos.getPrimerApellido())) {
            throw GestorLibreriaExcepcion.crear("El primer apellido del autor es obligatorio.", "El campo primerApellido llegó nulo en AutorDominio.");
        }
        if (!UtilTexto.tieneLongitudValida(datos.getPrimerApellido(), 1, 20)) {
            throw GestorLibreriaExcepcion.crear("El primer apellido del autor debe tener entre 1 y 20 caracteres.", "primerApellido con longitud inválida en AutorDominio: " + datos.getPrimerApellido().length() + SUFIJO_CARACTERES);
        }
        if (UtilObjeto.esNulo(datos.getSegundoApellido())) {
            throw GestorLibreriaExcepcion.crear("El segundo apellido del autor es obligatorio.", "El campo segundoApellido llegó nulo en AutorDominio.");
        }
        if (!datos.getSegundoApellido().isEmpty() && !UtilTexto.tieneLongitudValida(datos.getSegundoApellido(), 1, 20)) {
            throw GestorLibreriaExcepcion.crear("El segundo apellido del autor debe tener entre 1 y 20 caracteres si se indica.", "segundoApellido con longitud inválida en AutorDominio: " + datos.getSegundoApellido().length() + SUFIJO_CARACTERES);
        }
    }

    // P3 — Validar que el autor exista en el sistema
    private void validarExistencia(final UUID id) {
        final AutorEntidad entidad = daoFactory.getAutorDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El autor indicado no existe en el sistema.", "No se encontró Autor con id: " + id);
        }
    }

    private AutorEntidad construirEntidad(final AutorDominio datos) {
        return new AutorEntidad.Builder()
                .id(datos.getId())
                .primerNombre(datos.getPrimerNombre())
                .segundoNombre(datos.getSegundoNombre())
                .primerApellido(datos.getPrimerApellido())
                .segundoApellido(datos.getSegundoApellido())
                .build();
    }
}
