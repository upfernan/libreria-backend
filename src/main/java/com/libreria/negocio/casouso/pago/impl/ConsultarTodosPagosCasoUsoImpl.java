package com.libreria.negocio.casouso.pago.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.pago.ConsultarTodosPagosCasoUso;

public class ConsultarTodosPagosCasoUsoImpl implements ConsultarTodosPagosCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosPagosCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<PagoEntidad> ejecutar(final PagoEntidad filtro) {
        // P1 — Delegar la consulta al DAO con el filtro recibido
        return daoFactory.getPagoDAO().consultarPorFiltro(filtro);
    }
}
