package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.autorlibro.ConsultarAutorLibroPorIdCasoUso;

public class ConsultarAutorLibroPorIdCasoUsoImpl implements ConsultarAutorLibroPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarAutorLibroPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public AutorLibroEntidad ejecutar(final UUID id) {
        // P7 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        return daoFactory.getAutorLibroDAO().consultarPorId(id);
    }
}
