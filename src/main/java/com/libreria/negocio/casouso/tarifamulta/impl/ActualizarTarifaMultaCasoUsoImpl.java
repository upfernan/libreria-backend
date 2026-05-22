package com.libreria.negocio.casouso.tarifamulta.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.ActualizarTarifaMultaCasoUso;
import com.libreria.negocio.dominio.TarifaMultaDominio;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilNumero;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarTarifaMultaCasoUsoImpl implements ActualizarTarifaMultaCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarTarifaMultaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final TarifaMultaDominio datos) {
        // P4 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P5 — Validar que la tarifa de multa exista en el sistema
        validarExistencia(datos.getId());
        // P3 — Validar que el valor diario sea mayor que cero
        validarValorDiario(datos.getValorDiario());
        
        daoFactory.getTarifaMultaDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P4 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final TarifaMultaDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la tarifa de multa son obligatorios.", "Se recibió un objeto TarifaMultaDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador de la tarifa de multa es obligatorio.", "El campo id llegó nulo en TarifaMultaDominio.");
        }
        if (UtilNumero.esNulo(datos.getValorDiario())) {
            throw GestorLibreriaExcepcion.crear("El valor diario de la tarifa de multa es obligatorio.", "El campo valorDiario llegó nulo en TarifaMultaDominio.");
        }
        if (UtilFecha.esNula(datos.getFechaInicioVigencia()) || UtilFecha.FECHA_DEFECTO.equals(datos.getFechaInicioVigencia())) {
            throw GestorLibreriaExcepcion.crear("La fecha de inicio de vigencia de la tarifa de multa es obligatoria.", "El campo fechaInicioVigencia llegó nulo o sin valor en TarifaMultaDominio.");
        }
    }

    // P5 — Validar que la tarifa de multa exista en el sistema
    private void validarExistencia(final UUID id) {
        final TarifaMultaEntidad entidad = daoFactory.getTarifaMultaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La tarifa de multa indicada no existe en el sistema.", "No se encontró TarifaMulta con id: " + id);
        }
    }

    // P3 — El valor diario debe ser mayor que cero
    private void validarValorDiario(final Double valorDiario) {
        if (valorDiario <= 0.0) {
            throw GestorLibreriaExcepcion.crear("El valor diario de la tarifa de multa debe ser mayor que cero.", "valorDiario inválido en TarifaMultaDominio: " + valorDiario);
        }
    }

    private TarifaMultaEntidad construirEntidad(final TarifaMultaDominio datos) {
        return new TarifaMultaEntidad.Builder()
                .id(datos.getId())
                .valorDiario(datos.getValorDiario())
                .fechaInicioVigencia(datos.getFechaInicioVigencia())
                .fechaFinVigencia(datos.getFechaFinVigencia())
                .build();
    }
}
