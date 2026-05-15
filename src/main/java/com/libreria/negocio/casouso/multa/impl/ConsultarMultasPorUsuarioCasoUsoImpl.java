package com.libreria.negocio.casouso.multa.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.multa.ConsultarMultasPorUsuarioCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarMultasPorUsuarioCasoUsoImpl implements ConsultarMultasPorUsuarioCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarMultasPorUsuarioCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<MultaEntidad> ejecutar(final UUID usuarioId) {
        // P1 — Validar que el identificador del usuario sea obligatorio
        if (UtilObjeto.esNulo(usuarioId)) {
            throw GestorLibreriaExcepcion.crear("El identificador del usuario es obligatorio para consultar sus multas.", "usuarioId nulo en ConsultarMultasPorUsuarioCasoUso.");
        }
        // P2 — Delegar la consulta al DAO con el filtro de usuario
        final MultaEntidad filtro = new MultaEntidad.Builder()
                .usuarioAfectado(new UsuarioEntidad.Builder().id(usuarioId).build())
                .build();
        return daoFactory.getMultaDAO().consultarPorFiltro(filtro);
    }
}
