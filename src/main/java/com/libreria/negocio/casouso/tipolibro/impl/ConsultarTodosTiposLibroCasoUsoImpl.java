package com.libreria.negocio.casouso.tipolibro.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.tipolibro.ConsultarTodosTiposLibroCasoUso;

public class ConsultarTodosTiposLibroCasoUsoImpl implements ConsultarTodosTiposLibroCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosTiposLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<TipoLibroEntidad> ejecutar(final TipoLibroEntidad filtro) {
        // P6 — El filtro puede ser nulo (retorna todos), simplemente se delega al DAO
        return daoFactory.getTipoLibroDAO().consultarPorFiltro(filtro);
    }
}
