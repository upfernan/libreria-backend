package com.libreria.negocio.casouso.tarifamulta.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.AgregarTarifaMultaCasoUso;
import com.libreria.negocio.dominio.TarifaMultaDominio;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilNumero;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarTarifaMultaCasoUsoImpl implements AgregarTarifaMultaCasoUso {

    private final DAOFactory daoFactory;

    public AgregarTarifaMultaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final TarifaMultaDominio datos) {
        // P4 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P3 — Validar que el valor diario sea mayor que cero
        validarValorDiario(datos.getValorDiario());
        // P2 — Validar que la fecha de inicio de vigencia sea posterior a la de la tarifa actualmente vigente
        validarFechaInicioVigenciaPosteriorAVigente(datos.getFechaInicioVigencia());
        // P1 — Registrar la tarifa de multa garantizando identificador único
        guardarNuevaTarifaMulta(datos);
    }

    // P4 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final TarifaMultaDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la tarifa de multa son obligatorios.", "Se recibió un objeto TarifaMultaDominio nulo.");
        }
        if (UtilNumero.esNulo(datos.getValorDiario())) {
            throw GestorLibreriaExcepcion.crear("El valor diario de la tarifa de multa es obligatorio.", "El campo valorDiario llegó nulo en TarifaMultaDominio.");
        }
        if (UtilFecha.esNula(datos.getFechaInicioVigencia()) || UtilFecha.FECHA_DEFECTO.equals(datos.getFechaInicioVigencia())) {
            throw GestorLibreriaExcepcion.crear("La fecha de inicio de vigencia de la tarifa de multa es obligatoria.", "El campo fechaInicioVigencia llegó nulo o sin valor en TarifaMultaDominio.");
        }
    }

    // P3 — El valor diario debe ser mayor que cero
    private void validarValorDiario(final Double valorDiario) {
        if (valorDiario <= 0.0) {
            throw GestorLibreriaExcepcion.crear("El valor diario de la tarifa de multa debe ser mayor que cero.", "valorDiario inválido en TarifaMultaDominio: " + valorDiario);
        }
    }

    // P2 — La fecha de inicio de vigencia debe ser posterior a la de la tarifa actualmente vigente
    private void validarFechaInicioVigenciaPosteriorAVigente(final java.time.LocalDate fechaInicioVigencia) {
        final TarifaMultaEntidad tarifaVigente = obtenerTarifaVigente();
        if (!UtilObjeto.esNulo(tarifaVigente.getId()) && !fechaInicioVigencia.isAfter(tarifaVigente.getFechaInicioVigencia())) {
            throw GestorLibreriaExcepcion.crear(
                    "La fecha de inicio de vigencia de la nueva tarifa debe ser posterior a la de la tarifa actualmente vigente.",
                    "fechaInicioVigencia: " + fechaInicioVigencia + " no es posterior a la vigente: " + tarifaVigente.getFechaInicioVigencia());
        }
    }

    // P1 — Generar id único, cerrar vigente y persistir la nueva tarifa de multa
    private void guardarNuevaTarifaMulta(final TarifaMultaDominio datos) {
        UUID id = UtilUUID.generar();
        while (UtilUUID.tieneValor(daoFactory.getTarifaMultaDAO().consultarPorId(id).getId())) {
            id = UtilUUID.generar();
        }

        // Cerrar la tarifa vigente: asignarle como fechaFinVigencia la fechaInicioVigencia de la nueva
        final TarifaMultaEntidad tarifaVigente = obtenerTarifaVigente();
        if (!UtilObjeto.esNulo(tarifaVigente.getId())) {
            daoFactory.getTarifaMultaDAO().actualizar(tarifaVigente.getId(), new TarifaMultaEntidad.Builder()
                    .id(tarifaVigente.getId())
                    .valorDiario(tarifaVigente.getValorDiario())
                    .fechaInicioVigencia(tarifaVigente.getFechaInicioVigencia())
                    .fechaFinVigencia(datos.getFechaInicioVigencia())
                    .build());
        }

        // Crear la nueva tarifa con fechaFinVigencia = FECHA_DEFECTO (indica que está vigente)
        daoFactory.getTarifaMultaDAO().crear(new TarifaMultaEntidad.Builder()
                .id(id)
                .valorDiario(datos.getValorDiario())
                .fechaInicioVigencia(datos.getFechaInicioVigencia())
                .fechaFinVigencia(UtilFecha.FECHA_DEFECTO)
                .build());
    }

    // Obtener la tarifa vigente: aquella cuya fechaFinVigencia es FECHA_DEFECTO
    private TarifaMultaEntidad obtenerTarifaVigente() {
        final List<TarifaMultaEntidad> todasLasTarifas = daoFactory.getTarifaMultaDAO()
                .consultarPorFiltro(new TarifaMultaEntidad.Builder().build());
        if (UtilObjeto.esNulo(todasLasTarifas) || todasLasTarifas.isEmpty()) {
            return new TarifaMultaEntidad.Builder().build();
        }
        for (final TarifaMultaEntidad tarifa : todasLasTarifas) {
            if (UtilFecha.FECHA_DEFECTO.equals(tarifa.getFechaFinVigencia())) {
                return tarifa;
            }
        }
        return new TarifaMultaEntidad.Builder().build();
    }
}
