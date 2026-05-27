package com.libreria.negocio.casouso.prestamo.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PagoEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.prestamo.RetirarPrestamoCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarPrestamoCasoUsoImpl implements RetirarPrestamoCasoUso {

    private final DAOFactory daoFactory;

    public RetirarPrestamoCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P11 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador del préstamo es obligatorio.", "Se recibió un UUID nulo para retirar préstamo.");
        }
        // P9 — El préstamo debe estar registrado en el sistema
        final PrestamoEntidad prestamo = validarExistencia(id);
        // P12 — El préstamo no puede estar en estado activo
        validarNoActivo(prestamo);
        // P13 — El préstamo no puede eliminarse si tiene una multa registrada
        validarSinMultas(id);
        
        eliminarCascada(id);
      
        daoFactory.getPrestamoDAO().eliminar(id);
    }

    // P9 — El préstamo debe estar registrado en el sistema
    private PrestamoEntidad validarExistencia(final UUID id) {
        final PrestamoEntidad entidad = daoFactory.getPrestamoDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El préstamo indicado no existe en el sistema.", "No se encontró Préstamo con id: " + id);
        }
        return entidad;
    }

    // P12 — El préstamo no puede estar en estado activo
    private void validarNoActivo(final PrestamoEntidad prestamo) {
        if ("activo".equalsIgnoreCase(prestamo.getEstadoPrestamo().getNombre())) {
            throw GestorLibreriaExcepcion.crear("El préstamo no puede eliminarse porque está en estado activo.", "prestamoId: " + prestamo.getId());
        }
    }

    // P13 — El préstamo no puede eliminarse si tiene una multa registrada
    private void validarSinMultas(final UUID prestamoId) {
        final List<DevolucionEntidad> devoluciones = daoFactory.getDevolucionDAO()
                .consultarPorFiltro(new DevolucionEntidad.Builder()
                        .prestamo(new PrestamoEntidad.Builder().id(prestamoId).build())
                        .build());
        if (UtilObjeto.esNulo(devoluciones) || devoluciones.isEmpty()) {
            return;
        }
        for (final DevolucionEntidad devolucion : devoluciones) {
            final List<MultaEntidad> multas = daoFactory.getMultaDAO()
                    .consultarPorFiltro(new MultaEntidad.Builder()
                            .devolucion(new DevolucionEntidad.Builder().id(devolucion.getId()).build())
                            .build());
            if (!UtilObjeto.esNulo(multas) && !multas.isEmpty()) {
                throw GestorLibreriaExcepcion.crear(
                        "El préstamo no puede eliminarse porque tiene una multa registrada.",
                        "prestamoId: " + prestamoId + " tiene multa en devolucionId: " + devolucion.getId());
            }
        }
    }

  
    private void eliminarCascada(final UUID prestamoId) {
        final List<DevolucionEntidad> devoluciones = daoFactory.getDevolucionDAO()
                .consultarPorFiltro(new DevolucionEntidad.Builder()
                        .prestamo(new PrestamoEntidad.Builder().id(prestamoId).build())
                        .build());
        if (UtilObjeto.esNulo(devoluciones) || devoluciones.isEmpty()) {
            return;
        }
        for (final DevolucionEntidad devolucion : devoluciones) {
            eliminarMultasDeDevolucion(devolucion.getId());
            daoFactory.getDevolucionDAO().eliminar(devolucion.getId());
        }
    }

    private void eliminarMultasDeDevolucion(final UUID devolucionId) {
        final List<MultaEntidad> multas = daoFactory.getMultaDAO()
                .consultarPorFiltro(new MultaEntidad.Builder()
                        .devolucion(new DevolucionEntidad.Builder().id(devolucionId).build())
                        .build());
        if (UtilObjeto.esNulo(multas) || multas.isEmpty()) {
            return;
        }
        for (final MultaEntidad multa : multas) {
            eliminarPagosDeMulta(multa.getId());
            daoFactory.getMultaDAO().eliminar(multa.getId());
        }
    }

    private void eliminarPagosDeMulta(final UUID multaId) {
        final List<PagoEntidad> pagos = daoFactory.getPagoDAO()
                .consultarPorFiltro(new PagoEntidad.Builder()
                        .multa(new MultaEntidad.Builder().id(multaId).build())
                        .build());
        if (UtilObjeto.esNulo(pagos) || pagos.isEmpty()) {
            return;
        }
        for (final PagoEntidad pago : pagos) {
            daoFactory.getPagoDAO().eliminar(pago.getId());
        }
    }

}
