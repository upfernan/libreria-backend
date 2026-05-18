package com.libreria.negocio.casouso.ejemplar.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.negocio.casouso.ejemplar.ConsultarTodosEjemplaresCasoUso;

public class ConsultarTodosEjemplaresCasoUsoImpl implements ConsultarTodosEjemplaresCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosEjemplaresCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<EjemplarEntidad> ejecutar(final EjemplarEntidad filtro) {
        // El filtro puede ser nulo (retorna todos), simplemente se delega al DAO
        return daoFactory.getEjemplarDAO().consultarPorFiltro(filtro);
    }

}
