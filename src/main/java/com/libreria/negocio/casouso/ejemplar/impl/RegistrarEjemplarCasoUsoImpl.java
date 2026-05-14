package com.libreria.negocio.casouso.ejemplar.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.ejemplar.RegistrarEjemplarCasoUso;
import com.libreria.negocio.dominio.EjemplarDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarEjemplarCasoUsoImpl implements RegistrarEjemplarCasoUso {

	private final DAOFactory daoFactory;

	public RegistrarEjemplarCasoUsoImpl(final DAOFactory daoFactory) {
		super();
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final EjemplarDominio datos) {
		// P4 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
		validarDatosObligatorios(datos);

		// P2 — Validar que el libro existe en el sistema
		final LibroEntidad libro = validarExistenciaLibro(datos.getLibro().getId());

		// P3 — Resolver signatura: físico crea una nueva, no físico usa la semilla
		final SignaturaEntidad signatura = resolverSignatura(libro, datos.getSignatura());

		// P1 — Registrar el ejemplar garantizando identificador único
		guardarNuevoEjemplar(datos, signatura);
	}

	// P4 — Datos requeridos válidos en tipo, obligatoriedad y formato
	private void validarDatosObligatorios(final EjemplarDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw GestorLibreriaExcepcion.crear("Los datos del ejemplar son obligatorios.", "Se recibió un objeto EjemplarDominio nulo.");
		}
		if (UtilObjeto.esNulo(datos.getLibro()) || UtilUUID.esNulo(datos.getLibro().getId())) {
			throw GestorLibreriaExcepcion.crear("El libro del ejemplar es obligatorio.", "El campo libroId llegó nulo o vacío en EjemplarDominio.");
		}
		if (datos.getLibro().getId().equals(UtilUUID.UUID_DEFECTO)) {
			throw GestorLibreriaExcepcion.crear("El identificador del libro no es válido.", "El libroId es igual al UUID_DEFECTO.");
		}
	}

	// P2 — Validar que el libro exista en la base de datos y retornarlo para P3
	private LibroEntidad validarExistenciaLibro(final UUID libroId) {
		final LibroEntidad libro = daoFactory.getLibroDAO().consultarPorId(libroId);
		if (UtilObjeto.esNulo(libro) || UtilObjeto.esNulo(libro.getId())) {
			throw GestorLibreriaExcepcion.crear("El libro indicado no existe en el sistema.", "No se encontró registro de libro con id: " + libroId);
		}
		return libro;
	}

	// P3 — Físico: valida campos, verifica unicidad libro+signatura y crea la signatura. No físico: retorna la semilla.
	private SignaturaEntidad resolverSignatura(final LibroEntidad libro, final com.libreria.negocio.dominio.SignaturaDominio signaturaDominio) {
		final TipoLibroEntidad tipoLibro = daoFactory.getTipoLibroDAO().consultarPorId(libro.getTipoLibro().getId());
		if (!UtilObjeto.esNulo(tipoLibro) && "físico".equalsIgnoreCase(tipoLibro.getNombre())) {
			if (UtilObjeto.esNulo(signaturaDominio) || signaturaDominio.getPasillo() == '\0' || signaturaDominio.getPasillo() == ' ') {
				throw GestorLibreriaExcepcion.crear("Los ejemplares físicos deben tener una signatura asociada.", "El pasillo de la signatura llegó vacío para un libro de tipo físico.");
			}
			validarCombinacionLibroSignaturaUnica(libro.getId(), signaturaDominio);
			return crearNuevaSignatura(signaturaDominio);
		}
		return obtenerSignaturaPorDefecto();
	}

	// Verifica que no exista ya un ejemplar con la misma combinación libro + pasillo + estante + posicion
	private void validarCombinacionLibroSignaturaUnica(final UUID libroId, final com.libreria.negocio.dominio.SignaturaDominio signaturaDominio) {
		final SignaturaEntidad filtroSignatura = new SignaturaEntidad.Builder()
				.pasillo(signaturaDominio.getPasillo())
				.estante(signaturaDominio.getEstante())
				.posicion(signaturaDominio.getPosicion())
				.build();
		final List<SignaturaEntidad> signaturas = daoFactory.getSignaturaDAO().consultarPorFiltro(filtroSignatura);
		if (UtilObjeto.esNulo(signaturas) || signaturas.isEmpty()) {
			return;
		}
		for (final SignaturaEntidad signatura : signaturas) {
			final EjemplarEntidad filtroEjemplar = new EjemplarEntidad.Builder()
					.libro(new LibroEntidad.Builder().id(libroId).build())
					.signatura(signatura)
					.build();
			final List<EjemplarEntidad> ejemplares = daoFactory.getEjemplarDAO().consultarPorFiltro(filtroEjemplar);
			if (!UtilObjeto.esNulo(ejemplares) && !ejemplares.isEmpty()) {
				throw GestorLibreriaExcepcion.crear(
						"Ya existe un ejemplar con esa combinación de libro y signatura.",
						"libroId: " + libroId + ", signatura: " + signaturaDominio.getPasillo() + "/" + signaturaDominio.getEstante() + "/" + signaturaDominio.getPosicion());
			}
		}
	}

	// Crea y persiste una nueva signatura, retorna la entidad con su id generado
	private SignaturaEntidad crearNuevaSignatura(final com.libreria.negocio.dominio.SignaturaDominio signaturaDominio) {
		UUID id = UtilUUID.generar();
		while (!UtilObjeto.esNulo(daoFactory.getSignaturaDAO().consultarPorId(id))) {
			id = UtilUUID.generar();
		}
		final SignaturaEntidad nuevaSignatura = new SignaturaEntidad.Builder()
				.id(id)
				.pasillo(signaturaDominio.getPasillo())
				.estante(signaturaDominio.getEstante())
				.posicion(signaturaDominio.getPosicion())
				.build();
		daoFactory.getSignaturaDAO().crear(nuevaSignatura);
		return nuevaSignatura;
	}

	// Retorna la signatura semilla usada para libros no físicos (pasillo Z, estante 0, posición 0)
	private SignaturaEntidad obtenerSignaturaPorDefecto() {
		final SignaturaEntidad signatura = daoFactory.getSignaturaDAO()
				.consultarPorId(UUID.fromString("00000000-0000-0000-0005-000000000001"));
		if (UtilObjeto.esNulo(signatura) || UtilObjeto.esNulo(signatura.getId())) {
			throw GestorLibreriaExcepcion.crear("No se encontró la signatura por defecto requerida para libros no físicos.");
		}
		return signatura;
	}

	// P1 — Generar un identificador único para el ejemplar garantizando que no exista en la BD
	private UUID generarIdUnico() {
		UUID id = UtilUUID.generar();
		while (!UtilObjeto.esNulo(daoFactory.getEjemplarDAO().consultarPorId(id))) {
			id = UtilUUID.generar();
		}
		return id;
	}

	// P1 — Construir y persistir el nuevo ejemplar con id único garantizado
	private void guardarNuevoEjemplar(final EjemplarDominio datos, final SignaturaEntidad signatura) {
		final EjemplarEntidad nuevoEjemplar = new EjemplarEntidad.Builder()
				.id(generarIdUnico())
				.libro(new LibroEntidad.Builder()
						.id(datos.getLibro().getId())
						.build())
				.signatura(signatura)
				.build();

		daoFactory.getEjemplarDAO().crear(nuevoEjemplar);
	}

	public static void main(final String[] args) {
		System.out.println("=== Test Registrar Ejemplar ===");

		final DAOFactory factory = DAOFactory.getFactory();
		try {
			factory.iniciarTransaccion();

			// Libro físico: pasa pasillo, estante y posicion
			// Libro digital: omite signatura, se asigna la semilla automáticamente
			final UUID libroId = UUID.fromString("539dec14-ad5b-4dc0-832a-5753bda78a80");

			final EjemplarDominio dominio = new EjemplarDominio.Builder()
					.libro(new com.libreria.negocio.dominio.LibroDominio.Builder()
							.id(libroId)
							.build())
					.signatura(new com.libreria.negocio.dominio.SignaturaDominio.Builder()
							.pasillo('A')
							.estante(1)
							.posicion(1)
							.build())
					.build();

			new RegistrarEjemplarCasoUsoImpl(factory).ejecutar(dominio);

			factory.confirmarTransaccion();
			System.out.println("Ejemplar registrado exitosamente.");

		} catch (final Exception e) {
			factory.cancelarTransaccion();
			System.err.println("Error: " + e.getMessage());
		} finally {
			factory.cerrarConexion();
		}

		System.out.println("=== Fin del test ===");
	}

}
