package com.libreria.negocio.casouso.tarifamulta.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.ConsultarTodasTarifasMultaCasoUso;

public class ConsultarTodasTarifasMultaCasoUsoImpl implements ConsultarTodasTarifasMultaCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasTarifasMultaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<TarifaMultaEntidad> ejecutar(final TarifaMultaEntidad filtro) {
        // P8 Asegurar que los datos sean válidos a nivel de tipo de dato, formato, longitud y rango
        return daoFactory.getTarifaMultaDAO().consultarPorFiltro(filtro);
    }
}
