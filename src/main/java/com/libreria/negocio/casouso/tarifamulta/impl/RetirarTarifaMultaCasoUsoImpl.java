package com.libreria.negocio.casouso.tarifamulta.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.RetirarTarifaMultaCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarTarifaMultaCasoUsoImpl implements RetirarTarifaMultaCasoUso {

    private final DAOFactory daoFactory;

    public RetirarTarifaMultaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P5 — Validar que la tarifa de multa exista en el sistema
        validarExistencia(id);
        // P6 — Validar que no sea la única tarifa de multa registrada
        validarNoEsUnica(id);
        // P1 — Eliminar la tarifa de multa del sistema (P7: la anterior pasa a ser vigente automáticamente)
        daoFactory.getTarifaMultaDAO().eliminar(id);
    }

    // P5 — Validar que la tarifa de multa exista en el sistema
    private void validarExistencia(final UUID id) {
        final TarifaMultaEntidad entidad = daoFactory.getTarifaMultaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La tarifa de multa indicada no existe en el sistema.", "No se encontró TarifaMulta con id: " + id);
        }
    }

    // P6 — No se puede eliminar si es la única tarifa registrada
    private void validarNoEsUnica(final UUID id) {
        final List<TarifaMultaEntidad> todas = daoFactory.getTarifaMultaDAO().consultarPorFiltro(new TarifaMultaEntidad.Builder().build());
        if (UtilObjeto.esNulo(todas) || todas.size() <= 1) {
            throw GestorLibreriaExcepcion.crear("No se puede eliminar la única tarifa de multa registrada en el sistema.", "Intento de eliminar la última tarifaMulta con id: " + id);
        }
    }
}
