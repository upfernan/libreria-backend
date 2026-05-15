package com.libreria.negocio.casouso.tipoidentificacion.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.negocio.casouso.tipoidentificacion.ConsultarTodosTiposIdentificacionCasoUso;

public class ConsultarTodosTiposIdentificacionCasoUsoImpl implements ConsultarTodosTiposIdentificacionCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosTiposIdentificacionCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<TipoIdentificacionEntidad> ejecutar(final TipoIdentificacionEntidad filtro) {
        // P6 — El filtro puede ser nulo (retorna todos), simplemente se delega al DAO
        return daoFactory.getTipoIdentificacionDAO().consultarPorFiltro(filtro);
    }
}
