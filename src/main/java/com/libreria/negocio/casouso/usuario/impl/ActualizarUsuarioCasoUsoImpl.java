package com.libreria.negocio.casouso.usuario.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.usuario.ActualizarUsuarioCasoUso;
import com.libreria.negocio.dominio.UsuarioDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarUsuarioCasoUsoImpl implements ActualizarUsuarioCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarUsuarioCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UsuarioDominio datos) {
        // P4 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P5 — Validar que el usuario exista en el sistema
        validarExistenciaUsuario(datos.getId());
        // P3 — Validar que el tipo de identificación existe en el sistema
        validarExistenciaTipoIdentificacion(datos.getTipoIdentificacion().getId());
        // P2 — Validar que la combinación número + tipo de identificación no esté ya registrada en otro usuario
        validarCombinacionIdentificacionUnicaExcluyendo(datos.getNumeroIdentificacion(), datos.getTipoIdentificacion().getId(), datos.getId());
        // P1 — Actualizar el usuario en el sistema
        daoFactory.getUsuarioDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P4 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final UsuarioDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del usuario son obligatorios.", "Se recibió un objeto UsuarioDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del usuario es obligatorio.", "El campo id llegó nulo en UsuarioDominio.");
        }
        if (UtilObjeto.esNulo(datos.getTipoIdentificacion()) || UtilUUID.esNulo(datos.getTipoIdentificacion().getId())) {
            throw GestorLibreriaExcepcion.crear("El tipo de identificación es obligatorio.", "El campo tipoIdentificacionId llegó nulo o vacío en UsuarioDominio.");
        }
        if (datos.getTipoIdentificacion().getId().equals(UtilUUID.UUID_DEFECTO)) {
            throw GestorLibreriaExcepcion.crear("El tipo de identificación no es válido.", "El tipoIdentificacionId es igual al UUID_DEFECTO.");
        }
        if (UtilTexto.esNula(datos.getNumeroIdentificacion())) {
            throw GestorLibreriaExcepcion.crear("El número de identificación es obligatorio.", "El campo numeroIdentificacion llegó nulo o vacío en UsuarioDominio.");
        }
        if (UtilTexto.esNula(datos.getPrimerNombre())) {
            throw GestorLibreriaExcepcion.crear("El primer nombre es obligatorio.", "El campo primerNombre llegó nulo o vacío en UsuarioDominio.");
        }
        if (UtilTexto.esNula(datos.getPrimerApellido())) {
            throw GestorLibreriaExcepcion.crear("El primer apellido es obligatorio.", "El campo primerApellido llegó nulo o vacío en UsuarioDominio.");
        }
        if (UtilTexto.esNula(datos.getCorreoElectronico())) {
            throw GestorLibreriaExcepcion.crear("El correo electrónico es obligatorio.", "El campo correoElectronico llegó nulo o vacío en UsuarioDominio.");
        }
        if (!datos.getCorreoElectronico().contains("@") || !datos.getCorreoElectronico().contains(".")) {
            throw GestorLibreriaExcepcion.crear("El correo electrónico no tiene un formato válido.", "El correoElectronico no contiene '@' o '.' requeridos: " + datos.getCorreoElectronico());
        }
    }

    // P5 — Validar que el usuario exista en la base de datos
    private void validarExistenciaUsuario(final UUID id) {
        final UsuarioEntidad entidad = daoFactory.getUsuarioDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El usuario indicado no existe en el sistema.", "No se encontró usuario con id: " + id);
        }
    }

    // P3 — Validar que el tipo de identificación exista en la base de datos
    private void validarExistenciaTipoIdentificacion(final UUID tipoIdentificacionId) {
        final TipoIdentificacionEntidad tipoIdentificacion = daoFactory.getTipoIdentificacionDAO().consultarPorId(tipoIdentificacionId);
        if (UtilObjeto.esNulo(tipoIdentificacion) || UtilObjeto.esNulo(tipoIdentificacion.getId())) {
            throw GestorLibreriaExcepcion.crear("El tipo de identificación indicado no existe en el sistema.", "No se encontró registro de tipoIdentificacion con id: " + tipoIdentificacionId);
        }
    }

    // P2 — Validar que la combinación número + tipo de identificación no esté ya registrada en otro usuario
    private void validarCombinacionIdentificacionUnicaExcluyendo(final String numeroIdentificacion, final UUID tipoIdentificacionId, final UUID usuarioId) {
        final UsuarioEntidad filtro = new UsuarioEntidad.Builder()
                .numeroIdentificacion(numeroIdentificacion)
                .tipoIdentificacion(new TipoIdentificacionEntidad.Builder()
                        .id(tipoIdentificacionId)
                        .build())
                .build();
        final List<UsuarioEntidad> existentes = daoFactory.getUsuarioDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes)) {
            for (final UsuarioEntidad existente : existentes) {
                if (!existente.getId().equals(usuarioId)) {
                    throw GestorLibreriaExcepcion.crear(
                            "Ya existe otro usuario con ese número y tipo de identificación.",
                            "Combinación duplicada — numeroIdentificacion: " + numeroIdentificacion + ", tipoIdentificacionId: " + tipoIdentificacionId);
                }
            }
        }
    }

    private UsuarioEntidad construirEntidad(final UsuarioDominio datos) {
        return new UsuarioEntidad.Builder()
                .id(datos.getId())
                .tipoIdentificacion(new TipoIdentificacionEntidad.Builder()
                        .id(datos.getTipoIdentificacion().getId())
                        .build())
                .numeroIdentificacion(datos.getNumeroIdentificacion())
                .primerNombre(datos.getPrimerNombre())
                .segundoNombre(datos.getSegundoNombre())
                .primerApellido(datos.getPrimerApellido())
                .segundoApellido(datos.getSegundoApellido())
                .correoElectronico(datos.getCorreoElectronico())
                .build();
    }

}
