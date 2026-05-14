package com.libreria.negocio.casouso.libro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.libro.RegistrarLibroCasoUso;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarLibroCasoUsoImpl implements RegistrarLibroCasoUso {

	private final DAOFactory daoFactory;

	public RegistrarLibroCasoUsoImpl(final DAOFactory daoFactory) {
		super();
		this.daoFactory = daoFactory;
	}

	@Override
	public void ejecutar(final LibroDominio datos) {
		// P5 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
		validarDatosObligatorios(datos);

		// P2 — Validar que la categoría existe en el sistema
		validarExistenciaCategoria(datos.getCategoria().getId());

		// P3 — Validar que el tipo de libro existe en el sistema
		validarExistenciaTipoLibro(datos.getTipoLibro().getId());

		// P4 — Validar que la editorial existe en el sistema
		validarExistenciaEditorial(datos.getEditorial().getId());

		// P1 — Registrar el libro garantizando identificador único
		guardarNuevoLibro(datos);
	}

	// P5 — Datos requeridos válidos en tipo, obligatoriedad y formato
	private void validarDatosObligatorios(final LibroDominio datos) {
		if (UtilObjeto.esNulo(datos)) {
			throw GestorLibreriaExcepcion.crear("Los datos del libro son obligatorios.", "Se recibió un objeto LibroDominio nulo.");
		}
		if (UtilTexto.esNula(datos.getTitulo())) {
			throw GestorLibreriaExcepcion.crear("El título del libro es obligatorio.", "El campo titulo llegó nulo o vacío en LibroDominio.");
		}
		if (UtilObjeto.esNulo(datos.getTipoLibro()) || UtilUUID.esNulo(datos.getTipoLibro().getId())) {
			throw GestorLibreriaExcepcion.crear("El tipo de libro es obligatorio.", "El campo tipoLibroId llegó nulo o vacío en LibroDominio.");
		}
		if (datos.getTipoLibro().getId().equals(UtilUUID.UUID_DEFECTO)) {
			throw GestorLibreriaExcepcion.crear("El tipo de libro no es válido.", "El tipoLibroId es igual al UUID_DEFECTO.");
		}
		if (UtilObjeto.esNulo(datos.getCategoria()) || UtilUUID.esNulo(datos.getCategoria().getId())) {
			throw GestorLibreriaExcepcion.crear("La categoría del libro es obligatoria.", "El campo categoriaId llegó nulo o vacío en LibroDominio.");
		}
		if (datos.getCategoria().getId().equals(UtilUUID.UUID_DEFECTO)) {
			throw GestorLibreriaExcepcion.crear("La categoría del libro no es válida.", "El categoriaId es igual al UUID_DEFECTO.");
		}
		if (UtilObjeto.esNulo(datos.getEditorial()) || UtilUUID.esNulo(datos.getEditorial().getId())) {
			throw GestorLibreriaExcepcion.crear("La editorial del libro es obligatoria.", "El campo editorialId llegó nulo o vacío en LibroDominio.");
		}
		if (datos.getEditorial().getId().equals(UtilUUID.UUID_DEFECTO)) {
			throw GestorLibreriaExcepcion.crear("La editorial del libro no es válida.", "El editorialId es igual al UUID_DEFECTO.");
		}
		if (UtilObjeto.esNulo(datos.getDisponibles()) || datos.getDisponibles() < 0) {
			throw GestorLibreriaExcepcion.crear("La cantidad de disponibles debe ser un número mayor o igual a cero.", "El campo disponibles llegó nulo o negativo en LibroDominio.");
		}
	}

	// P3 — Validar que el tipo de libro exista en la base de datos
	private void validarExistenciaTipoLibro(final UUID tipoLibroId) {
		final TipoLibroEntidad tipoLibro = daoFactory.getTipoLibroDAO().consultarPorId(tipoLibroId);
		if (UtilObjeto.esNulo(tipoLibro) || UtilObjeto.esNulo(tipoLibro.getId())) {
			throw GestorLibreriaExcepcion.crear("El tipo de libro indicado no existe en el sistema.", "No se encontró registro de tipoLibro con id: " + tipoLibroId);
		}
	}

	// P2 — Validar que la categoría exista en la base de datos
	private void validarExistenciaCategoria(final UUID categoriaId) {
		final CategoriaEntidad categoria = daoFactory.getCategoriaDAO().consultarPorId(categoriaId);
		if (UtilObjeto.esNulo(categoria) || UtilObjeto.esNulo(categoria.getId())) {
			throw GestorLibreriaExcepcion.crear("La categoría indicada no existe en el sistema.", "No se encontró registro de categoria con id: " + categoriaId);
		}
	}

	// P4 — Validar que la editorial exista en la base de datos
	private void validarExistenciaEditorial(final UUID editorialId) {
		final EditorialEntidad editorial = daoFactory.getEditorialDAO().consultarPorId(editorialId);
		if (UtilObjeto.esNulo(editorial) || UtilObjeto.esNulo(editorial.getId())) {
			throw GestorLibreriaExcepcion.crear("La editorial indicada no existe en el sistema.", "No se encontró registro de editorial con id: " + editorialId);
		}
	}

	// P1 — Generar un identificador único garantizando que no exista en la BD
	private UUID generarIdUnico() {
		UUID id = UtilUUID.generar();
		while (!UtilObjeto.esNulo(daoFactory.getLibroDAO().consultarPorId(id))) {
			id = UtilUUID.generar();
		}
		return id;
	}

	// P1 — Construir y persistir el nuevo libro con id único garantizado
	private void guardarNuevoLibro(final LibroDominio datos) {
		final LibroEntidad nuevoLibro = new LibroEntidad.Builder()
				.id(generarIdUnico())
				.titulo(datos.getTitulo())
				.tipoLibro(new TipoLibroEntidad.Builder()
						.id(datos.getTipoLibro().getId())
						.build())
				.categoria(new CategoriaEntidad.Builder()
						.id(datos.getCategoria().getId())
						.build())
				.editorial(new EditorialEntidad.Builder()
						.id(datos.getEditorial().getId())
						.build())
				.disponibles(datos.getDisponibles())
				.build();

		daoFactory.getLibroDAO().crear(nuevoLibro);
	}

}
