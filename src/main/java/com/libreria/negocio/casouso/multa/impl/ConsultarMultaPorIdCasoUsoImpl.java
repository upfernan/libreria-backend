package com.libreria.negocio.casouso.multa.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.casouso.multa.ConsultarMultaPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarMultaPorIdCasoUsoImpl implements ConsultarMultaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarMultaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public MultaEntidad ejecutar(final UUID id) {
        // P5 Asegurar que el identificador único (ID) que se envía para llevar a cabo la acción sea válido a nivel de formato.
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar multa en ConsultarMultaPorIdCasoUsoImpl.");
        }
        final MultaEntidad resultado = daoFactory.getMultaDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró la multa con el identificador proporcionado.", "No existe registro en la tabla multa para el id: " + id + " en ConsultarMultaPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
