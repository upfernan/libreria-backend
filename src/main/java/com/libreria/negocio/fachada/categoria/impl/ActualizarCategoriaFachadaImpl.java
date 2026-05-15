package com.libreria.negocio.fachada.categoria.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.CategoriaDTO;
import com.libreria.negocio.casouso.categoria.ActualizarCategoriaCasoUso;
import com.libreria.negocio.casouso.categoria.impl.ActualizarCategoriaCasoUsoImpl;
import com.libreria.negocio.dominio.CategoriaDominio;
import com.libreria.negocio.fachada.categoria.ActualizarCategoriaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarCategoriaFachadaImpl implements ActualizarCategoriaFachada {

	private final DAOFactory daoFactory;
	private final ActualizarCategoriaCasoUso casoUso;

	public ActualizarCategoriaFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ActualizarCategoriaCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final CategoriaDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final CategoriaDominio dominio = new CategoriaDominio.Builder()
					.id(datos.getId())
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar la categoría.", "Error técnico inesperado en ActualizarCategoriaFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
