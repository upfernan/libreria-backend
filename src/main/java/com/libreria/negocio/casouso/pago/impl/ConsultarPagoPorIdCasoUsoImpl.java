package com.libreria.negocio.casouso.pago.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.pago.ConsultarPagoPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarPagoPorIdCasoUsoImpl implements ConsultarPagoPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarPagoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public PagoEntidad ejecutar(final UUID id) {
        validarFiltro(id);
        final PagoEntidad resultado = daoFactory.getPagoDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró el pago con el identificador proporcionado.", "No existe registro en la tabla pago para el id: " + id + " en ConsultarPagoPorIdCasoUsoImpl.");
        }
        return resultado;
    }

    // P6 — Asegurar que los datos enviados como filtro sean válidos en tipo de dato, longitud, obligatoriedad, formato y rango
    private void validarFiltro(final UUID id) {
        if (UtilUUID.esNulo(id) || !UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador del pago es obligatorio y debe ser un UUID válido para realizar la consulta.", "id nulo o defecto en ConsultarPagoPorId.");
        }
    }
}
