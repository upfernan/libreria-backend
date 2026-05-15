package com.libreria.negocio.fachada.editorial.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.editorial.RetirarEditorialCasoUso;
import com.libreria.negocio.casouso.editorial.impl.RetirarEditorialCasoUsoImpl;
import com.libreria.negocio.fachada.editorial.RetirarEditorialFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEditorialFachadaImpl implements RetirarEditorialFachada {

	private final DAOFactory daoFactory;
	private final RetirarEditorialCasoUso casoUso;

	public RetirarEditorialFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RetirarEditorialCasoUsoImpl(daoFactory);
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar la editorial.", "Error técnico inesperado en RetirarEditorialFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
