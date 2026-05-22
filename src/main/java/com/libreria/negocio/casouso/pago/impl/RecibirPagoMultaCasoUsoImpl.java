package com.libreria.negocio.casouso.pago.impl;

import java.time.LocalDate;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.pago.RecibirPagoMultaCasoUso;
import com.libreria.negocio.dominio.PagoDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RecibirPagoMultaCasoUsoImpl implements RecibirPagoMultaCasoUso {

    private final DAOFactory daoFactory;

    public RecibirPagoMultaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final PagoDominio datos) {
        // P4 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
        validarDatos(datos);

        // P2 — La multa debe estar registrada en el sistema
        final MultaEntidad multa = validarMultaExiste(datos.getMulta().getId());

        // P3 — La multa no puede estar ya pagada
        validarMultaNoPagada(multa);

        // P5 — El monto del pago corresponde al valor total de la multa (invariante del sistema)
        final double montoPago = multa.getMontoTotal();

        // P1 — Generar identificador único para el pago
        final UUID pagoId = generarIdUnico();
        final LocalDate fechaPago = LocalDate.now();

        daoFactory.getPagoDAO().crear(new PagoEntidad.Builder()
                .id(pagoId)
                .fechaPago(fechaPago)
                .multa(multa)
                .build());

        // Actualizar el estado pagada de la multa a verdadero
        daoFactory.getMultaDAO().actualizar(multa.getId(), new MultaEntidad.Builder()
                .id(multa.getId())
                .montoTotal(montoPago)
                .fechaGeneracion(multa.getFechaGeneracion())
                .pagada(true)
                .diasRetraso(multa.getDiasRetraso())
                .tarifaMulta(multa.getTarifaMulta())
                .devolucion(multa.getDevolucion())
                .usuarioAfectado(multa.getUsuarioAfectado())
                .build());
    }

    // P4 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
    private void validarDatos(final PagoDominio datos) {
        if (UtilObjeto.esNulo(datos) || UtilObjeto.esNulo(datos.getMulta()) || !UtilUUID.tieneValor(datos.getMulta().getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador de la multa es obligatorio para registrar un pago.", "multa.id nulo o defecto en PagoDominio.");
        }
    }

    // P2 — La multa debe estar registrada en el sistema
    private MultaEntidad validarMultaExiste(final UUID multaId) {
        final MultaEntidad multa = daoFactory.getMultaDAO().consultarPorId(multaId);
        if (UtilObjeto.esNulo(multa) || !UtilUUID.tieneValor(multa.getId())) {
            throw GestorLibreriaExcepcion.crear("La multa indicada no existe en el sistema.", "No se encontró Multa con id: " + multaId);
        }
        return multa;
    }

    // P3 — La multa no puede estar ya pagada
    private void validarMultaNoPagada(final MultaEntidad multa) {
        if (Boolean.TRUE.equals(multa.getPagada())) {
            throw GestorLibreriaExcepcion.crear("La multa indicada ya fue pagada anteriormente.", "multaId ya tiene pago registrado: " + multa.getId());
        }
    }

    // P1 — Generar UUID único para el pago
    private UUID generarIdUnico() {
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (UtilUUID.tieneValor(daoFactory.getPagoDAO().consultarPorId(id).getId()));
        return id;
    }
}
