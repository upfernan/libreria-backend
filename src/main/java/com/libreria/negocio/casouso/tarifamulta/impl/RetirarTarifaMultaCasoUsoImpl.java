package com.libreria.negocio.casouso.tarifamulta.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.RetirarTarifaMultaCasoUso;
import com.libreria.transversal.utilitario.UtilFecha;
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
        final TarifaMultaEntidad aEliminar = validarExistencia(id);
        // P6 — Validar que no sea la única tarifa de multa registrada
        validarNoEsUnica(id);
        // P7 — Si es la vigente, promover la anterior como nueva vigente
        promoverAnteriorSiEsVigente(aEliminar);
        
        daoFactory.getTarifaMultaDAO().eliminar(id);
    }

    // P5 — Validar que la tarifa de multa exista en el sistema
    private TarifaMultaEntidad validarExistencia(final UUID id) {
        final TarifaMultaEntidad entidad = daoFactory.getTarifaMultaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La tarifa de multa indicada no existe en el sistema.", "No se encontró TarifaMulta con id: " + id);
        }
        return entidad;
    }

    // P6 — No se puede eliminar si es la única tarifa registrada
    private void validarNoEsUnica(final UUID id) {
        final List<TarifaMultaEntidad> todas = daoFactory.getTarifaMultaDAO().consultarPorFiltro(new TarifaMultaEntidad.Builder().build());
        if (UtilObjeto.esNulo(todas) || todas.size() <= 1) {
            throw GestorLibreriaExcepcion.crear("No se puede eliminar la única tarifa de multa registrada en el sistema.", "Intento de eliminar la última tarifaMulta con id: " + id);
        }
    }

    // P7 — Si la tarifa a eliminar es la vigente, promover la anterior como nueva vigente
    private void promoverAnteriorSiEsVigente(final TarifaMultaEntidad aEliminar) {
        if (!UtilFecha.FECHA_DEFECTO.equals(aEliminar.getFechaFinVigencia())) {
            return; // No es la vigente, nada que hacer
        }
        final List<TarifaMultaEntidad> todas = daoFactory.getTarifaMultaDAO()
                .consultarPorFiltro(new TarifaMultaEntidad.Builder().build());
        
        TarifaMultaEntidad anterior = null;
        for (final TarifaMultaEntidad t : todas) {
            if (t.getId().equals(aEliminar.getId())) {
                continue;
            }
            if (t.getFechaInicioVigencia().isBefore(aEliminar.getFechaInicioVigencia())
                    && (anterior == null || t.getFechaInicioVigencia().isAfter(anterior.getFechaInicioVigencia()))) {
                anterior = t;
            }
        }
        if (anterior != null) {
            daoFactory.getTarifaMultaDAO().actualizar(anterior.getId(), new TarifaMultaEntidad.Builder()
                    .id(anterior.getId())
                    .valorDiario(anterior.getValorDiario())
                    .fechaInicioVigencia(anterior.getFechaInicioVigencia())
                    .fechaFinVigencia(UtilFecha.FECHA_DEFECTO)
                    .build());
        }
    }
}
