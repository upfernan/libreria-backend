package com.libreria.negocio.casouso.multa.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.multa.CobrarMultaCasoUso;
import com.libreria.negocio.dominio.MultaDominio;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class CobrarMultaCasoUsoImpl implements CobrarMultaCasoUso {

    private final DAOFactory daoFactory;

    public CobrarMultaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final MultaDominio datos) {
        // P5 — Validar que el identificador de la devolución sea obligatorio
        if (UtilObjeto.esNulo(datos.getDevolucion().getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador de la devolución es obligatorio para cobrar una multa.", "devolucion.id nulo en MultaDominio.");
        }

        // P2 — Validar que la devolución exista en el sistema
        final DevolucionEntidad devolucion = daoFactory.getDevolucionDAO().consultarPorId(datos.getDevolucion().getId());
        if (UtilObjeto.esNulo(devolucion) || UtilObjeto.esNulo(devolucion.getId())) {
            throw GestorLibreriaExcepcion.crear("La devolución indicada no existe en el sistema.", "No se encontró Devolucion con id: " + datos.getDevolucion().getId());
        }

        // P3 — Validar que el libro sea de tipo físico
        final String tipoLibroNombre = devolucion.getPrestamo().getEjemplar().getLibro().getTipoLibro().getNombre();
        if (!"físico".equalsIgnoreCase(tipoLibroNombre)) {
            throw GestorLibreriaExcepcion.crear("Solo se generan multas para préstamos de libros de tipo físico.", "tipoLibro no físico: " + tipoLibroNombre);
        }

        // P4 — Validar que no exista ya una multa para esta devolución (combinación única)
        validarSinMultaPrevia(datos.getDevolucion().getId());

        // P5b — Validar que la devolución haya ocurrido con días de retraso (proxy de era vencido)
        final PrestamoEntidad prestamo = devolucion.getPrestamo();
        final int diasRetraso = (int) ChronoUnit.DAYS.between(prestamo.getFechaDevolucionEsperada(), devolucion.getFechaDevolucion());
        if (diasRetraso <= 0) {
            throw GestorLibreriaExcepcion.crear("El préstamo no presentó retraso en la devolución y no corresponde generar una multa.", "diasRetraso calculado: " + diasRetraso);
        }

        // P6 — Validar que exista una tarifa de multa vigente
        final TarifaMultaEntidad tarifaVigente = obtenerTarifaVigente();
        if (UtilObjeto.esNulo(tarifaVigente.getId())) {
            throw GestorLibreriaExcepcion.crear("No existe una tarifa de multa vigente para calcular el monto.", "No se encontró TarifaMulta vigente en el sistema.");
        }

        // P1 — Generar identificador único para la multa
        final UUID multaId = generarIdUnico();
        final double montoTotal = diasRetraso * tarifaVigente.getValorDiario();
        final LocalDate fechaGeneracion = devolucion.getFechaDevolucion();

        daoFactory.getMultaDAO().crear(new MultaEntidad.Builder()
                .id(multaId)
                .montoTotal(montoTotal)
                .fechaGeneracion(fechaGeneracion)
                .pagada(false)
                .diasRetraso(diasRetraso)
                .tarifaMulta(tarifaVigente)
                .devolucion(devolucion)
                .usuarioAfectado(prestamo.getUsuario())
                .build());
    }

    // P4 — Validar ausencia de multa previa para la misma devolución
    private void validarSinMultaPrevia(final UUID devolucionId) {
        final MultaEntidad filtro = new MultaEntidad.Builder()
                .devolucion(new DevolucionEntidad.Builder().id(devolucionId).build())
                .build();
        final List<MultaEntidad> existentes = daoFactory.getMultaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("La devolución indicada ya tiene una multa registrada.", "devolucionId ya asociado a una multa: " + devolucionId);
        }
    }

    // P1 — Generar UUID único para la multa
    private UUID generarIdUnico() {
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (!UtilObjeto.esNulo(daoFactory.getMultaDAO().consultarPorId(id)));
        return id;
    }

    // Obtener la tarifa de multa vigente: aquella cuya fechaFinVigencia es FECHA_DEFECTO
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
