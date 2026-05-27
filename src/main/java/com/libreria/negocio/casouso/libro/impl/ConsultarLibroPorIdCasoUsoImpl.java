package com.libreria.negocio.casouso.libro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.libro.ConsultarLibroPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarLibroPorIdCasoUsoImpl implements ConsultarLibroPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarLibroPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }
    // P9 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.

    @Override
    public LibroEntidad ejecutar(final UUID id) {
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar libro en ConsultarLibroPorIdCasoUsoImpl.");
        }
        final LibroEntidad resultado = daoFactory.getLibroDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró el libro con el identificador proporcionado.", "No existe registro en la tabla libro para el id: " + id + " en ConsultarLibroPorIdCasoUsoImpl.");
        }
        return resultado;
    }

}
