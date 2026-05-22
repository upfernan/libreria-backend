package com.libreria.negocio.casouso.signatura.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.ConsultarSignaturaPorIdCasoUso;

public class ConsultarSignaturaPorIdCasoUsoImpl implements ConsultarSignaturaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarSignaturaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public SignaturaEntidad ejecutar(final UUID id) {
        // P7 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        return daoFactory.getSignaturaDAO().consultarPorId(id);
    }
}
