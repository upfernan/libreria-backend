package com.libreria.negocio.fachada.editorial.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EditorialDTO;
import com.libreria.negocio.casouso.editorial.AgregarEditorialCasoUso;
import com.libreria.negocio.casouso.editorial.impl.AgregarEditorialCasoUsoImpl;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.negocio.fachada.editorial.AgregarEditorialFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarEditorialFachadaImpl implements AgregarEditorialFachada {

	private final DAOFactory daoFactory;
	private final AgregarEditorialCasoUso casoUso;

	public AgregarEditorialFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new AgregarEditorialCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final EditorialDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final EditorialDominio dominio = new EditorialDominio.Builder()
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar la editorial.", "Error técnico inesperado en AgregarEditorialFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
