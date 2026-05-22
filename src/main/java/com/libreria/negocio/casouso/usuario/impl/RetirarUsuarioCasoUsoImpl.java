package com.libreria.negocio.casouso.usuario.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.usuario.RetirarUsuarioCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarUsuarioCasoUsoImpl implements RetirarUsuarioCasoUso {

    private final DAOFactory daoFactory;

    public RetirarUsuarioCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador del usuario es obligatorio.", "Se recibió un UUID nulo para retirar usuario.");
        }
        // P5 — Validar que el usuario exista en el sistema
        validarExistencia(id);
        // P6 — Validar que el usuario no tenga préstamos activos
        validarSinPrestamosActivos(id);
        // P7 — Validar que el usuario no tenga reservas activas
        validarSinReservasActivas(id);
        // P8 — Validar que el usuario no tenga multas pendientes de pago
        validarSinMultasPendientes(id);
        
        daoFactory.getUsuarioDAO().eliminar(id);
    }

    // P5 — Validar que el usuario exista en el sistema
    private void validarExistencia(final UUID id) {
        final UsuarioEntidad entidad = daoFactory.getUsuarioDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El usuario indicado no existe en el sistema.", "No se encontró Usuario con id: " + id);
        }
    }

    // P6 — Validar que el usuario no tenga préstamos activos
    private void validarSinPrestamosActivos(final UUID id) {
        final PrestamoEntidad filtro = new PrestamoEntidad.Builder()
                .usuario(new UsuarioEntidad.Builder().id(id).build())
                .estadoPrestamo(new EstadoPrestamoEntidad.Builder().nombre("activo").build())
                .build();
        final List<PrestamoEntidad> prestamosActivos = daoFactory.getPrestamoDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(prestamosActivos) && !prestamosActivos.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El usuario tiene préstamos activos y no puede eliminarse.", "usuarioId: " + id);
        }
    }

    // P7 — Validar que el usuario no tenga reservas activas
    private void validarSinReservasActivas(final UUID id) {
        final ReservaEntidad filtro = new ReservaEntidad.Builder()
                .usuario(new UsuarioEntidad.Builder().id(id).build())
                .estadoReserva(new EstadoReservaEntidad.Builder().nombre("pendiente").build())
                .build();
        final List<ReservaEntidad> reservasActivas = daoFactory.getReservaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(reservasActivas) && !reservasActivas.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El usuario tiene reservas activas y no puede eliminarse.", "usuarioId: " + id);
        }
    }

    // P8 — Validar que el usuario no tenga multas sin pagar
    private void validarSinMultasPendientes(final UUID id) {
        final MultaEntidad filtro = new MultaEntidad.Builder()
                .usuarioAfectado(new UsuarioEntidad.Builder().id(id).build())
                .pagada(false)
                .build();
        final List<MultaEntidad> multasPendientes = daoFactory.getMultaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(multasPendientes) && !multasPendientes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El usuario tiene multas pendientes de pago y no puede eliminarse.", "usuarioId: " + id);
        }
    }

}
