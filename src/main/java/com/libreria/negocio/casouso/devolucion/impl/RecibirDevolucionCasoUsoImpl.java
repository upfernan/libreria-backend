package com.libreria.negocio.casouso.devolucion.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.devolucion.RecibirDevolucionCasoUso;
import com.libreria.negocio.dominio.DevolucionDominio;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RecibirDevolucionCasoUsoImpl implements RecibirDevolucionCasoUso {

    private final DAOFactory daoFactory;

    public RecibirDevolucionCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final DevolucionDominio datos) {
        // P4 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
        validarDatos(datos);

        // P2 — Validar que el préstamo exista en el sistema
        final PrestamoEntidad prestamo = daoFactory.getPrestamoDAO().consultarPorId(datos.getPrestamo().getId());
        if (UtilObjeto.esNulo(prestamo) || UtilObjeto.esNulo(prestamo.getId())) {
            throw GestorLibreriaExcepcion.crear("El préstamo indicado no existe en el sistema.", "No se encontró Prestamo con id: " + datos.getPrestamo().getId());
        }

        // P3 — Validar que el préstamo esté en estado activo o vencido
        final String estadoNombre = prestamo.getEstadoPrestamo().getNombre();
        if (!"activo".equalsIgnoreCase(estadoNombre) && !"vencido".equalsIgnoreCase(estadoNombre)) {
            throw GestorLibreriaExcepcion.crear("El préstamo no está en estado activo o vencido y no puede registrarse la devolución.", "estadoPrestamo inválido para devolución: " + estadoNombre);
        }
        final boolean eraVencido = "vencido".equalsIgnoreCase(estadoNombre);

        // P5 — El préstamo no puede tener una devolución previamente registrada
        validarSinDevolucionPrevia(datos.getPrestamo().getId());

        // Determinar si el libro es físico (aplica multa, disponibles y reservas)
        final boolean esLibroFisico = "físico".equalsIgnoreCase(prestamo.getEjemplar().getLibro().getTipoLibro().getNombre());

        // P1 — Generar identificador único para la devolución
        final UUID devolucionId = generarIdUnicoDevolucion();
        final LocalDate fechaDevolucion = LocalDate.now();

        // Crear la devolución con fecha autogenerada
        daoFactory.getDevolucionDAO().crear(new DevolucionEntidad.Builder()
                .id(devolucionId)
                .fechaDevolucion(fechaDevolucion)
                .prestamo(prestamo)
                .build());

        // P6 — Al registrar la devolución, el estado del préstamo cambia a devuelto
        cambiarEstadoPrestamo(prestamo, "devuelto");

        // P7 — El ejemplar queda disponible para préstamo después de registrar la devolución
        if (esLibroFisico) {
            incrementarDisponiblesLibro(prestamo);
        }

        // Generar multa si el préstamo era vencido y el libro es físico
        if (eraVencido && esLibroFisico) {
            generarMulta(prestamo, devolucionId, fechaDevolucion);
        }

        // P8 — Si existen reservas pendientes, la más antigua pasa a estado disponible
        if (esLibroFisico) {
            asignarReservaActiva(prestamo);
        }
    }

    // P4 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
    private void validarDatos(final DevolucionDominio datos) {
        if (UtilObjeto.esNulo(datos) || UtilObjeto.esNulo(datos.getPrestamo()) || UtilObjeto.esNulo(datos.getPrestamo().getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del préstamo es obligatorio para registrar una devolución.", "prestamo.id nulo en DevolucionDominio.");
        }
    }

    // P5 — Validar ausencia de devolución previa para el mismo préstamo
    private void validarSinDevolucionPrevia(final UUID prestamoId) {
        final DevolucionEntidad filtro = new DevolucionEntidad.Builder()
                .prestamo(new PrestamoEntidad.Builder().id(prestamoId).build())
                .build();
        final List<DevolucionEntidad> existentes = daoFactory.getDevolucionDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El préstamo indicado ya tiene una devolución registrada.", "prestamoId ya asociado a una devolución: " + prestamoId);
        }
    }

    // P1 — Generar UUID único para la devolución
    private UUID generarIdUnicoDevolucion() {
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (UtilUUID.tieneValor(daoFactory.getDevolucionDAO().consultarPorId(id).getId()));
        return id;
    }

    // P6 — Al registrar la devolución, el estado del préstamo cambia a devuelto
    private void cambiarEstadoPrestamo(final PrestamoEntidad prestamo, final String nombreEstado) {
        final List<EstadoPrestamoEntidad> estados = daoFactory.getEstadoPrestamoDAO()
                .consultarPorFiltro(new EstadoPrestamoEntidad.Builder().nombre(nombreEstado).build());
        if (UtilObjeto.esNulo(estados) || estados.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("No se encontró el estado de préstamo '" + nombreEstado + "' en el sistema.", "EstadoPrestamo no configurado: " + nombreEstado);
        }
        daoFactory.getPrestamoDAO().actualizar(prestamo.getId(), new PrestamoEntidad.Builder()
                .id(prestamo.getId())
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionEsperada(prestamo.getFechaDevolucionEsperada())
                .estadoPrestamo(estados.get(0))
                .reserva(prestamo.getReserva())
                .usuario(prestamo.getUsuario())
                .ejemplar(prestamo.getEjemplar())
                .build());
    }

    // P7 — El ejemplar queda disponible para préstamo después de registrar la devolución
    private void incrementarDisponiblesLibro(final PrestamoEntidad prestamo) {
        final LibroEntidad libro = prestamo.getEjemplar().getLibro();
        daoFactory.getLibroDAO().actualizar(libro.getId(), new LibroEntidad.Builder()
                .id(libro.getId())
                .titulo(libro.getTitulo())
                .tipoLibro(libro.getTipoLibro())
                .categoria(libro.getCategoria())
                .editorial(libro.getEditorial())
                .disponibles(libro.getDisponibles() + 1)
                .build());
    }

    // Generar la multa calculada por días de retraso
    private void generarMulta(final PrestamoEntidad prestamo, final UUID devolucionId, final LocalDate fechaDevolucion) {
        final TarifaMultaEntidad tarifaVigente = obtenerTarifaVigente();
        if (UtilObjeto.esNulo(tarifaVigente.getId())) {
            throw GestorLibreriaExcepcion.crear("No existe una tarifa de multa vigente para calcular el monto.", "No se encontró TarifaMulta vigente en el sistema.");
        }
        final int diasRetraso = (int) ChronoUnit.DAYS.between(prestamo.getFechaDevolucionEsperada(), fechaDevolucion);
        final double montoTotal = diasRetraso * tarifaVigente.getValorDiario();

        UUID multaId;
        do {
            multaId = UUID.randomUUID();
        } while (!UtilObjeto.esNulo(daoFactory.getMultaDAO().consultarPorId(multaId)));

        daoFactory.getMultaDAO().crear(new MultaEntidad.Builder()
                .id(multaId)
                .montoTotal(montoTotal)
                .fechaGeneracion(fechaDevolucion)
                .pagada(false)
                .diasRetraso(diasRetraso)
                .tarifaMulta(tarifaVigente)
                .devolucion(new DevolucionEntidad.Builder().id(devolucionId).build())
                .usuarioAfectado(prestamo.getUsuario())
                .build());
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

    // P8 — Si existen reservas pendientes, la más antigua pasa a estado disponible
    private void asignarReservaActiva(final PrestamoEntidad prestamo) {
        final LibroEntidad libro = prestamo.getEjemplar().getLibro();
        // Consultar solo reservas pendientes del libro, ordenadas por fechaReserva ASC
        final List<ReservaEntidad> reservasPendientes = daoFactory.getReservaDAO()
                .consultarPorFiltro(new ReservaEntidad.Builder()
                        .libro(new LibroEntidad.Builder().id(libro.getId()).build())
                        .estadoReserva(new EstadoReservaEntidad.Builder().nombre("pendiente").build())
                        .build());
        if (UtilObjeto.esNulo(reservasPendientes) || reservasPendientes.isEmpty()) {
            return;
        }

        // La primera en la lista es la más antigua (ORDER BY fechaReserva ASC del DAO)
        final ReservaEntidad reservaMasAntigua = reservasPendientes.get(0);

        final List<EstadoReservaEntidad> estadosDisponible = daoFactory.getEstadoReservaDAO()
                .consultarPorFiltro(new EstadoReservaEntidad.Builder().nombre("disponible").build());
        if (UtilObjeto.esNulo(estadosDisponible) || estadosDisponible.isEmpty()) {
            return;
        }

        daoFactory.getReservaDAO().actualizar(reservaMasAntigua.getId(), new ReservaEntidad.Builder()
                .estadoReserva(estadosDisponible.get(0))
                .build());
    }
}
