package com.libreria.negocio.casouso.signatura.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.ConsultarTodasSignaturasCasoUso;

public class ConsultarTodasSignaturasCasoUsoImpl implements ConsultarTodasSignaturasCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodasSignaturasCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<SignaturaEntidad> ejecutar(final SignaturaEntidad filtro) {
        // P7 — Delegar la consulta al DAO con el filtro recibido
        return daoFactory.getSignaturaDAO().consultarPorFiltro(filtro);
    }
}
