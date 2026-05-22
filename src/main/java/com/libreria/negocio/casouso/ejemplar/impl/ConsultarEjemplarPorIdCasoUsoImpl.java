package com.libreria.negocio.casouso.ejemplar.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.negocio.casouso.ejemplar.ConsultarEjemplarPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarEjemplarPorIdCasoUsoImpl implements ConsultarEjemplarPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarEjemplarPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }
    // P7 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.

    @Override
    public EjemplarEntidad ejecutar(final UUID id) {
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo para consultar ejemplar.");
        }
        return daoFactory.getEjemplarDAO().consultarPorId(id);
    }

}
