package com.libreria.negocio.casouso.tipoidentificacion.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.negocio.casouso.tipoidentificacion.ConsultarTipoIdentificacionPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTipoIdentificacionPorIdCasoUsoImpl implements ConsultarTipoIdentificacionPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTipoIdentificacionPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public TipoIdentificacionEntidad ejecutar(final UUID id) {
        // P6 — Asegura que los datos requeridos sean validos a nivel de tipo, formato y obligatoriedad
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar tipo de identificación en ConsultarTipoIdentificacionPorIdCasoUsoImpl.");
        }
        final TipoIdentificacionEntidad resultado = daoFactory.getTipoIdentificacionDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró el tipo de identificación con el identificador proporcionado.", "No existe registro en la tabla tipoidentificacion para el id: " + id + " en ConsultarTipoIdentificacionPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
