package com.libreria.negocio.casouso.estadoreserva.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.negocio.casouso.estadoreserva.ConsultarTodosEstadosReservaCasoUso;

public class ConsultarTodosEstadosReservaCasoUsoImpl implements ConsultarTodosEstadosReservaCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosEstadosReservaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<EstadoReservaEntidad> ejecutar(final EstadoReservaEntidad filtro) {
        // P6 Asegura que los datos requeridos sean validos a nivel de tipo, formato y obligatoriedad
        return daoFactory.getEstadoReservaDAO().consultarPorFiltro(filtro);
    }
}
