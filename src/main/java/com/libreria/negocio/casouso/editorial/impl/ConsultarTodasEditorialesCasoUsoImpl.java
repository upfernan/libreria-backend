package com.libreria.negocio.casouso.editorial.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.casouso.editorial.ConsultarTodasEditorialesCasoUso;

public class ConsultarTodasEditorialesCasoUsoImpl implements ConsultarTodasEditorialesCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasEditorialesCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<EditorialEntidad> ejecutar(final EditorialEntidad filtro) {
        // P6 Asegurar que los datos sean válidos a nivel de tipo de dato, formato, longitud y rango
        return daoFactory.getEditorialDAO().consultarPorFiltro(filtro);
    }
}
