package com.libreria.negocio.casouso.tipoidentificacion.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.tipoidentificacion.RetirarTipoIdentificacionCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarTipoIdentificacionCasoUsoImpl implements RetirarTipoIdentificacionCasoUso {

    private final DAOFactory daoFactory;

    public RetirarTipoIdentificacionCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P4 — Validar que el tipo de identificación exista en el sistema
        validarExistencia(id);
        // P5 — Validar que el tipo de identificación no esté en uso
        validarNoEnUso(id);
        
        daoFactory.getTipoIdentificacionDAO().eliminar(id);
    }

    // P4 — Validar que el tipo de identificación exista en el sistema
    private void validarExistencia(final UUID id) {
        final TipoIdentificacionEntidad entidad = daoFactory.getTipoIdentificacionDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El tipo de identificación indicado no existe en el sistema.", "No se encontró TipoIdentificacion con id: " + id);
        }
    }

    // P5 — Validar que el tipo de identificación no esté siendo utilizado por algún usuario
    private void validarNoEnUso(final UUID id) {
        final UsuarioEntidad filtroUsuario = new UsuarioEntidad.Builder()
                .tipoIdentificacion(new TipoIdentificacionEntidad.Builder().id(id).build()).build();
        final List<UsuarioEntidad> usuarios = daoFactory.getUsuarioDAO().consultarPorFiltro(filtroUsuario);
        if (!UtilObjeto.esNulo(usuarios) && !usuarios.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El tipo de identificación está en uso y no puede eliminarse.", "tipoIdentificacionId: " + id);
        }
    }
}
