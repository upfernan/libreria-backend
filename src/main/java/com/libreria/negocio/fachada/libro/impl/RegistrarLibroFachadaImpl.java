package com.libreria.negocio.fachada.libro.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.LibroDTO;
import com.libreria.negocio.casouso.libro.RegistrarLibroCasoUso;
import com.libreria.negocio.casouso.libro.impl.RegistrarLibroCasoUsoImpl;
import com.libreria.negocio.dominio.CategoriaDominio;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.negocio.dominio.TipoLibroDominio;
import com.libreria.negocio.fachada.libro.RegistrarLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarLibroFachadaImpl implements RegistrarLibroFachada {

	private DAOFactory daoFactory;
	private RegistrarLibroCasoUso casoUso;

	public RegistrarLibroFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RegistrarLibroCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final LibroDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final LibroDominio dominio = new LibroDominio.Builder()
					.titulo(datos.getTitulo())
					.tipoLibro(new TipoLibroDominio.Builder()
							.id(datos.getTipoLibro().getId())
							.build())
					.categoria(new CategoriaDominio.Builder()
							.id(datos.getCategoria().getId())
							.build())
					.editorial(new EditorialDominio.Builder()
							.id(datos.getEditorial().getId())
							.build())
					.disponibles(datos.getDisponibles())
					.build();

			casoUso.ejecutar(dominio);

			daoFactory.confirmarTransaccion();

		} catch (GestorLibreriaExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (Exception excepcion) {
			daoFactory.cancelarTransaccion();
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar el libro.", "Error técnico inesperado en RegistrarLibroFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
