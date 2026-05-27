package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.autorlibro.ConsultarAutorLibroPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarAutorLibroPorIdCasoUsoImpl implements ConsultarAutorLibroPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarAutorLibroPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public AutorLibroEntidad ejecutar(final UUID id) {
        // P7 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar autor-libro en ConsultarAutorLibroPorIdCasoUsoImpl.");
        }
        final AutorLibroEntidad resultado = daoFactory.getAutorLibroDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró la relación autor-libro con el identificador proporcionado.", "No existe registro en la tabla autorlibro para el id: " + id + " en ConsultarAutorLibroPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
