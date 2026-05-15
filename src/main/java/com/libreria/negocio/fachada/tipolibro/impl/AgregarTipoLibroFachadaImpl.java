package com.libreria.negocio.fachada.tipolibro.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoLibroDTO;
import com.libreria.negocio.casouso.tipolibro.AgregarTipoLibroCasoUso;
import com.libreria.negocio.casouso.tipolibro.impl.AgregarTipoLibroCasoUsoImpl;
import com.libreria.negocio.dominio.TipoLibroDominio;
import com.libreria.negocio.fachada.tipolibro.AgregarTipoLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarTipoLibroFachadaImpl implements AgregarTipoLibroFachada {

	private final DAOFactory daoFactory;
	private final AgregarTipoLibroCasoUso casoUso;

	public AgregarTipoLibroFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new AgregarTipoLibroCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final TipoLibroDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final TipoLibroDominio dominio = new TipoLibroDominio.Builder()
					.nombre(datos.getNombre())
					.descripcion(datos.getDescripcion())
					.build();

			casoUso.ejecutar(dominio);

			daoFactory.confirmarTransaccion();

		} catch (GestorLibreriaExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (Exception excepcion) {
			daoFactory.cancelarTransaccion();
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar el tipo de libro.", "Error técnico inesperado en AgregarTipoLibroFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
