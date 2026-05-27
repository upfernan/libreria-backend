package com.libreria.negocio.casouso.autor.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.casouso.autor.ConsultarAutorPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarAutorPorIdCasoUsoImpl implements ConsultarAutorPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarAutorPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public AutorEntidad ejecutar(final UUID id) {
        // P5 Asegurar que los datos sean válidos a nivel de tipo de dato, formato, longitud y rango
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar autor en ConsultarAutorPorIdCasoUsoImpl.");
        }
        final AutorEntidad resultado = daoFactory.getAutorDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró el autor con el identificador proporcionado.", "No existe registro en la tabla autor para el id: " + id + " en ConsultarAutorPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
