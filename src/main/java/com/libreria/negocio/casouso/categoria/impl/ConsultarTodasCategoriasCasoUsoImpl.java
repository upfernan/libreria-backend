package com.libreria.negocio.casouso.categoria.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.negocio.casouso.categoria.ConsultarTodasCategoriasCasoUso;

public class ConsultarTodasCategoriasCasoUsoImpl implements ConsultarTodasCategoriasCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasCategoriasCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<CategoriaEntidad> ejecutar(final CategoriaEntidad filtro) {
        // P6 Asegura que los datos requeridos sean validos a nivel de tipo, formato y obligatoriedad
        return daoFactory.getCategoriaDAO().consultarPorFiltro(filtro);
    }
}
