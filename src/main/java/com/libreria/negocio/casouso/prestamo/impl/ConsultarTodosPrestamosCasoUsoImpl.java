package com.libreria.negocio.casouso.prestamo.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.prestamo.ConsultarTodosPrestamosCasoUso;

public class ConsultarTodosPrestamosCasoUsoImpl implements ConsultarTodosPrestamosCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosPrestamosCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<PrestamoEntidad> ejecutar(final PrestamoEntidad filtro) {
        // El filtro puede ser nulo (retorna todos), simplemente se delega al DAO
        return daoFactory.getPrestamoDAO().consultarPorFiltro(filtro);
    }

}
