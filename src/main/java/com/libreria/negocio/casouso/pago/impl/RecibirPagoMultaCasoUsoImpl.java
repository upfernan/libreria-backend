package com.libreria.negocio.casouso.pago.impl;

import java.time.LocalDate;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.pago.RecibirPagoMultaCasoUso;
import com.libreria.negocio.dominio.PagoDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RecibirPagoMultaCasoUsoImpl implements RecibirPagoMultaCasoUso {

    private final DAOFactory daoFactory;

    public RecibirPagoMultaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final PagoDominio datos) {
        // P4 — Validar que el identificador de la multa sea obligatorio
        if (UtilObjeto.esNulo(datos.getMulta().getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador de la multa es obligatorio para registrar un pago.", "multa.id nulo en PagoDominio.");
        }

        // P2 — Validar que la multa exista en el sistema
        final MultaEntidad multa = daoFactory.getMultaDAO().consultarPorId(datos.getMulta().getId());
        if (UtilObjeto.esNulo(multa) || UtilObjeto.esNulo(multa.getId())) {
            throw GestorLibreriaExcepcion.crear("La multa indicada no existe en el sistema.", "No se encontró Multa con id: " + datos.getMulta().getId());
        }

        // P3 — Validar que la multa no esté ya pagada
        if (Boolean.TRUE.equals(multa.getPagada())) {
            throw GestorLibreriaExcepcion.crear("La multa indicada ya fue pagada anteriormente.", "multaId ya tiene pago registrado: " + multa.getId());
        }

        // P1 — Generar identificador único para el pago
        final UUID pagoId = generarIdUnico();
        final LocalDate fechaPago = LocalDate.now();

        // Crear el pago con fecha autogenerada
        daoFactory.getPagoDAO().crear(new PagoEntidad.Builder()
                .id(pagoId)
                .fechaPago(fechaPago)
                .multa(multa)
                .build());

        // P5 — Actualizar el estado pagada de la multa a verdadero
        daoFactory.getMultaDAO().actualizar(multa.getId(), new MultaEntidad.Builder()
                .id(multa.getId())
                .montoTotal(multa.getMontoTotal())
                .fechaGeneracion(multa.getFechaGeneracion())
                .pagada(true)
                .diasRetraso(multa.getDiasRetraso())
                .tarifaMulta(multa.getTarifaMulta())
                .devolucion(multa.getDevolucion())
                .usuarioAfectado(multa.getUsuarioAfectado())
                .build());
    }

    // P1 — Generar UUID único para el pago
    private UUID generarIdUnico() {
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (!UtilObjeto.esNulo(daoFactory.getPagoDAO().consultarPorId(id)));
        return id;
    }
}
