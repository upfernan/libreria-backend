package com.libreria.negocio.casouso.reserva.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.reserva.RegistrarReservaCasoUso;
import com.libreria.negocio.dominio.ReservaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarReservaCasoUsoImpl implements RegistrarReservaCasoUso {

	private final DAOFactory daoFactory;

	public RegistrarReservaCasoUsoImpl(final DAOFactory daoFactory) {
		super();
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final ReservaDominio datos) {
		// P6 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
		validarDatosObligatorios(datos);

		// P2 — Validar que el usuario existe en el sistema
		validarExistenciaUsuario(datos.getUsuario().getId());

		// P3 — Validar que el usuario no tenga multas pendientes
		validarUsuarioSinMultasPendientes(datos.getUsuario().getId());

		// P4 — Validar que el libro existe en el sistema
		validarExistenciaLibro(datos.getLibro().getId());

		// P5 — Validar que el usuario no tenga ya una reserva activa para ese libro
		validarReservaActivaUnica(datos.getUsuario().getId(), datos.getLibro().getId());

		// P1 — Registrar la reserva garantizando identificador único
		guardarNuevaReserva(datos);
	}

	// P6 — Datos requeridos válidos en tipo, obligatoriedad y formato
	private void validarDatosObligatorios(final ReservaDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw GestorLibreriaExcepcion.crear("Los datos de la reserva son obligatorios.", "Se recibió un objeto ReservaDominio nulo.");
		}
		if (UtilObjeto.esNulo(datos.getUsuario()) || UtilUUID.esNulo(datos.getUsuario().getId())) {
			throw GestorLibreriaExcepcion.crear("El usuario de la reserva es obligatorio.", "El campo usuarioId llegó nulo o vacío en ReservaDominio.");
		}
		if (datos.getUsuario().getId().equals(UtilUUID.UUID_DEFECTO)) {
			throw GestorLibreriaExcepcion.crear("El identificador del usuario no es válido.", "El usuarioId es igual al UUID_DEFECTO.");
		}
		if (UtilObjeto.esNulo(datos.getLibro()) || UtilUUID.esNulo(datos.getLibro().getId())) {
			throw GestorLibreriaExcepcion.crear("El libro de la reserva es obligatorio.", "El campo libroId llegó nulo o vacío en ReservaDominio.");
		}
		if (datos.getLibro().getId().equals(UtilUUID.UUID_DEFECTO)) {
			throw GestorLibreriaExcepcion.crear("El identificador del libro no es válido.", "El libroId es igual al UUID_DEFECTO.");
		}
	}

	// P2 — Validar que el usuario exista en la base de datos
	private void validarExistenciaUsuario(final UUID usuarioId) {
		final UsuarioEntidad usuario = daoFactory.getUsuarioDAO().consultarPorId(usuarioId);
		if (UtilObjeto.esNulo(usuario) || UtilObjeto.esNulo(usuario.getId())) {
			throw GestorLibreriaExcepcion.crear("El usuario indicado no existe en el sistema.", "No se encontró registro de usuario con id: " + usuarioId);
		}
	}

	// P3 — Validar que el usuario no tenga multas pendientes de pago
	private void validarUsuarioSinMultasPendientes(final UUID usuarioId) {
		final MultaEntidad filtroMulta = new MultaEntidad.Builder()
				.usuarioAfectado(new UsuarioEntidad.Builder().id(usuarioId).build())
				.build();
		final List<MultaEntidad> multasPendientes = daoFactory.getMultaDAO().consultarPorFiltro(filtroMulta);
		if (!UtilObjeto.esNulo(multasPendientes) && !multasPendientes.isEmpty()) {
			throw GestorLibreriaExcepcion.crear("El usuario tiene multas pendientes y no puede realizar una reserva.", "El usuario con id " + usuarioId + " tiene " + multasPendientes.size() + " multa(s) pendiente(s) sin pagar.");
		}
	}

	// P4 — Validar que el libro exista en la base de datos
	private void validarExistenciaLibro(final UUID libroId) {
		final LibroEntidad libro = daoFactory.getLibroDAO().consultarPorId(libroId);
		if (UtilObjeto.esNulo(libro) || UtilObjeto.esNulo(libro.getId())) {
			throw GestorLibreriaExcepcion.crear("El libro indicado no existe en el sistema.", "No se encontró registro de libro con id: " + libroId);
		}
	}

	// P5 — Validar que el usuario no tenga ya una reserva pendiente para ese libro
	private void validarReservaActivaUnica(final UUID usuarioId, final UUID libroId) {
		final ReservaEntidad filtroReserva = new ReservaEntidad.Builder()
				.usuario(new UsuarioEntidad.Builder().id(usuarioId).build())
				.libro(new LibroEntidad.Builder().id(libroId).build())
				.estadoReserva(new EstadoReservaEntidad.Builder().nombre("pendiente").build())
				.build();
		final List<ReservaEntidad> reservasActivas = daoFactory.getReservaDAO().consultarPorFiltro(filtroReserva);
		if (!UtilObjeto.esNulo(reservasActivas) && !reservasActivas.isEmpty()) {
			throw GestorLibreriaExcepcion.crear("El usuario ya tiene una reserva activa para ese libro.", "Ya existe una reserva pendiente para usuarioId: " + usuarioId + " y libroId: " + libroId);
		}
	}

	// P1 — Generar un identificador único garantizando que no exista en la BD
	private UUID generarIdUnico() {
		UUID id = UtilUUID.generar();
		while (!UtilObjeto.esNulo(daoFactory.getReservaDAO().consultarPorId(id))) {
			id = UtilUUID.generar();
		}
		return id;
	}

	// P1 — Construir y persistir la nueva reserva con id único garantizado
	private void guardarNuevaReserva(final ReservaDominio datos) {
		final List<EstadoReservaEntidad> estados = daoFactory.getEstadoReservaDAO()
				.consultarPorFiltro(new EstadoReservaEntidad.Builder().nombre("pendiente").build());
		if (UtilObjeto.esNulo(estados) || estados.isEmpty()) {
			throw GestorLibreriaExcepcion.crear("No se encontró el estado 'pendiente' requerido para registrar la reserva.");
		}

		final LocalDate fechaReserva = LocalDate.now();
		final LocalDate fechaExpiracion = fechaReserva.plusDays(7);

		final ReservaEntidad nuevaReserva = new ReservaEntidad.Builder()
				.id(generarIdUnico())
				.fechaReserva(fechaReserva)
				.fechaExpiracion(fechaExpiracion)
				.estadoReserva(estados.get(0))
				.usuario(new UsuarioEntidad.Builder().id(datos.getUsuario().getId()).build())
				.libro(new LibroEntidad.Builder().id(datos.getLibro().getId()).build())
				.build();

		daoFactory.getReservaDAO().crear(nuevaReserva);
	}

	public static void main(final String[] args) {
		System.out.println("=== Test Registrar Reserva ===");

		final DAOFactory factory = DAOFactory.getFactory();
		try {
			factory.iniciarTransaccion();

			// Reemplaza con IDs reales de tu BD
			// SELECT id FROM usuario WHERE numeroIdentificacion = '4125215'
			// SELECT id FROM libro
			final UUID usuarioId = UUID.fromString("237dd266-2efb-48f0-b8ac-20d01ac48e39");
			final UUID libroId   = UUID.fromString("539dec14-ad5b-4dc0-832a-5753bda78a80");

			final ReservaDominio dominio = new ReservaDominio.Builder()
					.usuario(new com.libreria.negocio.dominio.UsuarioDominio.Builder()
							.id(usuarioId)
							.build())
					.libro(new com.libreria.negocio.dominio.LibroDominio.Builder()
							.id(libroId)
							.build())
					.build();

			new RegistrarReservaCasoUsoImpl(factory).ejecutar(dominio);

			factory.confirmarTransaccion();
			System.out.println("Reserva registrada exitosamente.");

		} catch (final Exception e) {
			factory.cancelarTransaccion();
			System.err.println("Error: " + e.getMessage());
		} finally {
			factory.cerrarConexion();
		}

		System.out.println("=== Fin del test ===");
	}

}
