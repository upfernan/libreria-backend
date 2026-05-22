package com.libreria.negocio.casouso.signatura.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.ConsultarTodasSignaturasCasoUso;

public class ConsultarTodasSignaturasCasoUsoImpl implements ConsultarTodasSignaturasCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasSignaturasCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<SignaturaEntidad> ejecutar(final SignaturaEntidad filtro) {
        // P7 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        return daoFactory.getSignaturaDAO().consultarPorFiltro(filtro);
    }
}
