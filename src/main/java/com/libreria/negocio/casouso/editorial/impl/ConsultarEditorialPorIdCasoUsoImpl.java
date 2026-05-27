package com.libreria.negocio.casouso.editorial.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.casouso.editorial.ConsultarEditorialPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarEditorialPorIdCasoUsoImpl implements ConsultarEditorialPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarEditorialPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public EditorialEntidad ejecutar(final UUID id) {
        // P6 Asegurar que los datos sean válidos a nivel de tipo de dato, formato, longitud y rango
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar editorial en ConsultarEditorialPorIdCasoUsoImpl.");
        }
        final EditorialEntidad resultado = daoFactory.getEditorialDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró la editorial con el identificador proporcionado.", "No existe registro en la tabla editorial para el id: " + id + " en ConsultarEditorialPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
