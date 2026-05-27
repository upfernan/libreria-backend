package com.libreria.negocio.casouso.tarifamulta.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.ConsultarTarifaMultaPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTarifaMultaPorIdCasoUsoImpl implements ConsultarTarifaMultaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTarifaMultaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public TarifaMultaEntidad ejecutar(final UUID id) {
        // P8 Asegurar que los datos sean válidos a nivel de tipo de dato, formato, longitud y rango.
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar tarifa de multa en ConsultarTarifaMultaPorIdCasoUsoImpl.");
        }
        final TarifaMultaEntidad resultado = daoFactory.getTarifaMultaDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró la tarifa de multa con el identificador proporcionado.", "No existe registro en la tabla tarifamulta para el id: " + id + " en ConsultarTarifaMultaPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
