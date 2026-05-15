package com.libreria.negocio.casouso.reserva.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.casouso.reserva.ConsultarTodasReservasCasoUso;

public class ConsultarTodasReservasCasoUsoImpl implements ConsultarTodasReservasCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasReservasCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<ReservaEntidad> ejecutar(final ReservaEntidad filtro) {
        // El filtro puede ser nulo (retorna todas), simplemente se delega al DAO
        return daoFactory.getReservaDAO().consultarPorFiltro(filtro);
    }

}
