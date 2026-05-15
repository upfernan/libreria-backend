package com.libreria.negocio.casouso.estadoprestamo.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.negocio.casouso.estadoprestamo.ConsultarTodosEstadosPrestamoCasoUso;

public class ConsultarTodosEstadosPrestamoCasoUsoImpl implements ConsultarTodosEstadosPrestamoCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosEstadosPrestamoCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<EstadoPrestamoEntidad> ejecutar(final EstadoPrestamoEntidad filtro) {
        // P6 — El filtro puede ser nulo (retorna todos), simplemente se delega al DAO
        return daoFactory.getEstadoPrestamoDAO().consultarPorFiltro(filtro);
    }
}
