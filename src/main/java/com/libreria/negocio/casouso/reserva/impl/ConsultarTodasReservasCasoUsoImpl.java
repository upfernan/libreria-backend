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
        // P9 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        return daoFactory.getReservaDAO().consultarPorFiltro(filtro);
    }

}
