package com.libreria.negocio.casouso.prestamo.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.prestamo.RegistrarPrestamoCasoUso;
import com.libreria.negocio.dominio.PrestamoDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilPrestamo;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarPrestamoCasoUsoImpl implements RegistrarPrestamoCasoUso {

	private final DAOFactory daoFactory;

	public RegistrarPrestamoCasoUsoImpl(final DAOFactory daoFactory) {
		super();
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final PrestamoDominio datos) {
		// P6 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
		validarDatosObligatorios(datos);

		// P2 — El usuario debe estar registrado en el sistema
		validarExistenciaUsuario(datos.getUsuario().getId());

		// P3 — El usuario no puede tener multas pendientes de pago
		validarUsuarioSinMultasPendientes(datos.getUsuario().getId());

		// P8 — El usuario no puede superar el límite de préstamos simultáneos permitidos
		validarLimitePrestamosUsuario(datos.getUsuario().getId());

		// P4 — El ejemplar debe estar registrado en el sistema
		final EjemplarEntidad ejemplar = validarExistenciaEjemplar(datos.getEjemplar().getId());

		// P5 — El ejemplar no puede tener un préstamo activo
		validarEjemplarDisponible(datos.getEjemplar().getId());

		// Combinación única: no puede existir otro préstamo con el mismo usuario + ejemplar + fechaPréstamo
		final LocalDate fechaPrestamo = LocalDate.now();
		validarCombinacionUnica(datos.getUsuario().getId(), datos.getEjemplar().getId(), fechaPrestamo);

		// P7 — Si el ejemplar tiene reservas activas, se debe respetar el orden de la cola para asignarlo
		final ReservaEntidad reservaPendiente = validarColaDeReservas(datos.getUsuario().getId(), ejemplar.getLibro().getId());

		// P1 — No puede existir otro préstamo con el mismo identificador
		guardarNuevoPrestamo(datos, reservaPendiente, fechaPrestamo);

		// Marcar la reserva como atendida si el préstamo se originó desde una reserva real no "sin reserva" RECORDAR
		if (esReservaReal(reservaPendiente)) {
			marcarReservaComoAtendida(reservaPendiente);
		}
	}

	// P6 — Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
	private void validarDatosObligatorios(final PrestamoDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw GestorLibreriaExcepcion.crear("Los datos del préstamo son obligatorios.", "Se recibió un objeto PrestamoDominio nulo.");
		}
		if (UtilObjeto.esNulo(datos.getUsuario()) || UtilUUID.esNulo(datos.getUsuario().getId())) {
			throw GestorLibreriaExcepcion.crear("El identificador del usuario es obligatorio.", "El campo usuarioId llegó nulo o vacío en PrestamoDominio.");
		}
		if (datos.getUsuario().getId().equals(UtilUUID.UUID_DEFECTO)) {
			throw GestorLibreriaExcepcion.crear("El identificador del usuario no es válido.", "El usuarioId es igual al UUID_DEFECTO, no tiene formato válido.");
		}
		if (UtilObjeto.esNulo(datos.getEjemplar()) || UtilUUID.esNulo(datos.getEjemplar().getId())) {
			throw GestorLibreriaExcepcion.crear("El identificador del ejemplar es obligatorio.", "El campo ejemplarId llegó nulo o vacío en PrestamoDominio.");
		}
		if (datos.getEjemplar().getId().equals(UtilUUID.UUID_DEFECTO)) {
			throw GestorLibreriaExcepcion.crear("El identificador del ejemplar no es válido.", "El ejemplarId es igual al UUID_DEFECTO, no tiene formato válido.");
		}
	}

	// P8 — El usuario no puede superar el límite de préstamos simultáneos permitidos
	private void validarLimitePrestamosUsuario(final UUID usuarioId) {
		final List<PrestamoEntidad> activos = daoFactory.getPrestamoDAO()
				.consultarPorFiltro(new PrestamoEntidad.Builder()
						.usuario(new UsuarioEntidad.Builder().id(usuarioId).build())
						.estadoPrestamo(new EstadoPrestamoEntidad.Builder().nombre("activo").build())
						.build());
		final List<PrestamoEntidad> vencidos = daoFactory.getPrestamoDAO()
				.consultarPorFiltro(new PrestamoEntidad.Builder()
						.usuario(new UsuarioEntidad.Builder().id(usuarioId).build())
						.estadoPrestamo(new EstadoPrestamoEntidad.Builder().nombre("vencido").build())
						.build());
		final int total = (UtilObjeto.esNulo(activos) ? 0 : activos.size())
				+ (UtilObjeto.esNulo(vencidos) ? 0 : vencidos.size());
		if (total >= UtilPrestamo.LIMITE_PRESTAMOS_POR_USUARIO) {
			throw GestorLibreriaExcepcion.crear(
					"El usuario ya tiene el máximo de préstamos permitidos (" + UtilPrestamo.LIMITE_PRESTAMOS_POR_USUARIO + " activos o vencidos).",
					"usuarioId: " + usuarioId + ", préstamos activos/vencidos: " + total);
		}
	}

	// P2 — El usuario debe estar registrado en el sistema
	private void validarExistenciaUsuario(final UUID usuarioId) {
		final UsuarioEntidad usuario = daoFactory.getUsuarioDAO().consultarPorId(usuarioId);
		if (UtilObjeto.esNulo(usuario) || UtilObjeto.esNulo(usuario.getId())) {
			throw GestorLibreriaExcepcion.crear("El usuario indicado no existe en el sistema.", "No se encontró registro de usuario con id: " + usuarioId);
		}
	}

	// P3 — El usuario no puede tener multas pendientes de pago
	private void validarUsuarioSinMultasPendientes(final UUID usuarioId) {
		final MultaEntidad filtroMulta = new MultaEntidad.Builder()
				.usuarioAfectado(new UsuarioEntidad.Builder().id(usuarioId).build())
				.pagada(false)
				.build();
		final var multasPendientes = daoFactory.getMultaDAO().consultarPorFiltro(filtroMulta);
		if (!UtilObjeto.esNulo(multasPendientes) && !multasPendientes.isEmpty()) {
			throw GestorLibreriaExcepcion.crear("El usuario tiene multas pendientes de pago.", "El usuario con id " + usuarioId + " tiene " + multasPendientes.size() + " multa(s) pendiente(s) sin pagar.");
		}
	}

	// P4 — El ejemplar debe estar registrado en el sistema
	private EjemplarEntidad validarExistenciaEjemplar(final UUID ejemplarId) {
		final EjemplarEntidad ejemplar = daoFactory.getEjemplarDAO().consultarPorId(ejemplarId);
		if (UtilObjeto.esNulo(ejemplar) || UtilObjeto.esNulo(ejemplar.getId())) {
			throw GestorLibreriaExcepcion.crear("El ejemplar indicado no existe en el sistema.", "No se encontró registro de ejemplar con id: " + ejemplarId);
		}
		return ejemplar;
	}

	// P5 — El ejemplar no puede tener un préstamo activo
	private void validarEjemplarDisponible(final UUID ejemplarId) {
		final PrestamoEntidad filtroPrestamo = new PrestamoEntidad.Builder()
				.ejemplar(new EjemplarEntidad.Builder().id(ejemplarId).build())
				.estadoPrestamo(new EstadoPrestamoEntidad.Builder().nombre("activo").build())
				.build();
		final List<PrestamoEntidad> prestamosActivos = daoFactory.getPrestamoDAO().consultarPorFiltro(filtroPrestamo);
		if (!UtilObjeto.esNulo(prestamosActivos) && !prestamosActivos.isEmpty()) {
			throw GestorLibreriaExcepcion.crear("El ejemplar no está disponible para préstamo.", "El ejemplar con id " + ejemplarId + " ya tiene un préstamo en estado activo.");
		}
	}

	// Combinación única — Validar que no exista otro préstamo con el mismo usuario + ejemplar + fechaPréstamo
	private void validarCombinacionUnica(final UUID usuarioId, final UUID ejemplarId, final LocalDate fechaPrestamo) {
		final PrestamoEntidad filtro = new PrestamoEntidad.Builder()
				.usuario(new UsuarioEntidad.Builder().id(usuarioId).build())
				.ejemplar(new EjemplarEntidad.Builder().id(ejemplarId).build())
				.fechaPrestamo(fechaPrestamo)
				.build();
		final List<PrestamoEntidad> existentes = daoFactory.getPrestamoDAO().consultarPorFiltro(filtro);
		if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
			throw GestorLibreriaExcepcion.crear(
					"Ya existe un préstamo registrado para ese usuario, ejemplar y fecha.",
					"Combinación duplicada — usuarioId: " + usuarioId + ", ejemplarId: " + ejemplarId + ", fechaPrestamo: " + fechaPrestamo);
		}
	}

	// P7 — Si el ejemplar tiene reservas activas, se debe respetar el orden de la cola para asignarlo
	private ReservaEntidad validarColaDeReservas(final UUID usuarioId, final UUID libroId) {
		final ReservaEntidad filtroReserva = new ReservaEntidad.Builder()
				.libro(new LibroEntidad.Builder().id(libroId).build())
				.estadoReserva(new EstadoReservaEntidad.Builder().nombre("pendiente").build())
				.build();
		final List<ReservaEntidad> reservasPendientes = daoFactory.getReservaDAO().consultarPorFiltro(filtroReserva);

		if (UtilObjeto.esNulo(reservasPendientes) || reservasPendientes.isEmpty()) {
			return obtenerReservaSinReserva();
		}

		final ReservaEntidad primeraReserva = reservasPendientes.get(0);
		if (!primeraReserva.getUsuario().getId().equals(usuarioId)) {
			throw GestorLibreriaExcepcion.crear("No es posible realizar el préstamo. Hay reservas previas pendientes para este libro.", "El usuario " + usuarioId + " no es el primero en la cola de reservas del libro " + libroId + ".");
		}
		return primeraReserva;
	}

	// Determina si la reserva es una reserva real (estado "pendiente") y no el registro "sin reserva"
	private boolean esReservaReal(final ReservaEntidad reserva) {
		final ReservaEntidad filtroSinReserva = new ReservaEntidad.Builder()
				.estadoReserva(new EstadoReservaEntidad.Builder().nombre("sin reserva").build())
				.build();
		final List<ReservaEntidad> sinReservaList = daoFactory.getReservaDAO().consultarPorFiltro(filtroSinReserva);
		if (UtilObjeto.esNulo(sinReservaList) || sinReservaList.isEmpty()) {
			return true;
		}
		return !reserva.getId().equals(sinReservaList.get(0).getId());
	}

	// Marcar la reserva como atendida dentro de la misma transacción del préstamo
	private void marcarReservaComoAtendida(final ReservaEntidad reserva) {
		final EstadoReservaEntidad filtroEstado = new EstadoReservaEntidad.Builder()
				.nombre("atendida")
				.build();
		final List<EstadoReservaEntidad> estados = daoFactory.getEstadoReservaDAO().consultarPorFiltro(filtroEstado);

		if (UtilObjeto.esNulo(estados) || estados.isEmpty()) {
			throw GestorLibreriaExcepcion.crear("No se encontró el estado 'atendida' para actualizar la reserva.");
		}

		final ReservaEntidad reservaAtendida = new ReservaEntidad.Builder()
				.estadoReserva(estados.get(0))
				.build();
		daoFactory.getReservaDAO().actualizar(reserva.getId(), reservaAtendida);
	}

	// P1 — No puede existir otro préstamo con el mismo identificador (generación de id único garantizado)
	private UUID generarIdUnico() {
		UUID id = UtilUUID.generar();
		while (!UtilObjeto.esNulo(daoFactory.getPrestamoDAO().consultarPorId(id))) {
			id = UtilUUID.generar();
		}
		return id;
	}

	// P1 — No puede existir otro préstamo con el mismo identificador (construcción y persistencia)
	private void guardarNuevoPrestamo(final PrestamoDominio datos, final ReservaEntidad reserva, final LocalDate fechaPrestamo) {
		final EstadoPrestamoEntidad filtroEstado = new EstadoPrestamoEntidad.Builder()
				.nombre("activo")
				.build();
		final List<EstadoPrestamoEntidad> estados = daoFactory.getEstadoPrestamoDAO().consultarPorFiltro(filtroEstado);

		if (UtilObjeto.esNulo(estados) || estados.isEmpty()) {
			throw GestorLibreriaExcepcion.crear("No se encontró el estado 'activo' requerido para registrar el préstamo.");
		}

		final LocalDate fechaDevolucionEsperada = fechaPrestamo.plusDays(14);

		final PrestamoEntidad nuevoPrestamo = new PrestamoEntidad.Builder()
				.id(generarIdUnico())
				.fechaPrestamo(fechaPrestamo)
				.fechaDevolucionEsperada(fechaDevolucionEsperada)
				.estadoPrestamo(estados.get(0))
				.reserva(reserva)
				.usuario(new UsuarioEntidad.Builder().id(datos.getUsuario().getId()).build())
				.ejemplar(new EjemplarEntidad.Builder().id(datos.getEjemplar().getId()).build())
				.build();

		daoFactory.getPrestamoDAO().crear(nuevoPrestamo);
	}

	// Obtiene el registro de reserva semilla que representa "sin reserva" (dato obligatorio en préstamo)
	private ReservaEntidad obtenerReservaSinReserva() {
		final ReservaEntidad filtro = new ReservaEntidad.Builder()
				.estadoReserva(new EstadoReservaEntidad.Builder().nombre("sin reserva").build())
				.build();
		final List<ReservaEntidad> resultados = daoFactory.getReservaDAO().consultarPorFiltro(filtro);
		if (UtilObjeto.esNulo(resultados) || resultados.isEmpty()) {
			throw GestorLibreriaExcepcion.crear("No se encontró el registro de reserva 'sin reserva' requerido para préstamos sin reserva previa.");
		}
		return resultados.get(0);
	}


}
