package com.libreria.negocio.casouso.pago.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.pago.ConsultarTodosPagosCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosPagosCasoUsoImpl implements ConsultarTodosPagosCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosPagosCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<PagoEntidad> ejecutar(final PagoEntidad filtro) {
        // P6 — Asegurar que los datos enviados como filtro sean válidos en tipo de dato, longitud, obligatoriedad, formato y rango
        validarFiltro(filtro);
        return daoFactory.getPagoDAO().consultarPorFiltro(filtro);
    }

    // P6 — Asegurar que los datos enviados como filtro sean válidos en tipo de dato, longitud, obligatoriedad, formato y rango
    private void validarFiltro(final PagoEntidad filtro) {
        if (UtilObjeto.esNulo(filtro)) {
            throw GestorLibreriaExcepcion.crear("Los datos de filtro son inválidos para realizar la consulta de pagos.", "filtro nulo en ConsultarTodosPagos.");
        }
    }
}
