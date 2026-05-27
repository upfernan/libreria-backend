package com.libreria.negocio.casouso.tipolibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.tipolibro.ConsultarTipoLibroPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTipoLibroPorIdCasoUsoImpl implements ConsultarTipoLibroPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTipoLibroPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public TipoLibroEntidad ejecutar(final UUID id) {
        // P6 Asegura que los datos requeridos sean validos a nivel de tipo, formato y obligatoriedad
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar tipo de libro en ConsultarTipoLibroPorIdCasoUsoImpl.");
        }
        final TipoLibroEntidad resultado = daoFactory.getTipoLibroDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró el tipo de libro con el identificador proporcionado.", "No existe registro en la tabla tipolibro para el id: " + id + " en ConsultarTipoLibroPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
