package com.libreria.negocio.fachada.tipolibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.tipolibro.RetirarTipoLibroCasoUso;
import com.libreria.negocio.casouso.tipolibro.impl.RetirarTipoLibroCasoUsoImpl;
import com.libreria.negocio.fachada.tipolibro.RetirarTipoLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarTipoLibroFachadaImpl implements RetirarTipoLibroFachada {

	private final DAOFactory daoFactory;
	private final RetirarTipoLibroCasoUso casoUso;

	public RetirarTipoLibroFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RetirarTipoLibroCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final UUID id) {
		try {
			daoFactory.iniciarTransaccion();

			casoUso.ejecutar(id);

			daoFactory.confirmarTransaccion();

		} catch (GestorLibreriaExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (Exception excepcion) {
			daoFactory.cancelarTransaccion();
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar el tipo de libro.", "Error técnico inesperado en RetirarTipoLibroFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
