package com.libreria.negocio.fachada.editorial.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EditorialDTO;
import com.libreria.negocio.casouso.editorial.ActualizarEditorialCasoUso;
import com.libreria.negocio.casouso.editorial.impl.ActualizarEditorialCasoUsoImpl;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.negocio.fachada.editorial.ActualizarEditorialFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEditorialFachadaImpl implements ActualizarEditorialFachada {

	private final DAOFactory daoFactory;
	private final ActualizarEditorialCasoUso casoUso;

	public ActualizarEditorialFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ActualizarEditorialCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final EditorialDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final EditorialDominio dominio = new EditorialDominio.Builder()
					.id(datos.getId())
					.nit(datos.getNit())
					.nombre(datos.getNombre())
					.build();

			casoUso.ejecutar(dominio);

			daoFactory.confirmarTransaccion();

		} catch (GestorLibreriaExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (Exception excepcion) {
			daoFactory.cancelarTransaccion();
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar la editorial.", "Error técnico inesperado en ActualizarEditorialFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
