package com.libreria.negocio.fachada.categoria.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.categoria.RetirarCategoriaCasoUso;
import com.libreria.negocio.casouso.categoria.impl.RetirarCategoriaCasoUsoImpl;
import com.libreria.negocio.fachada.categoria.RetirarCategoriaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarCategoriaFachadaImpl implements RetirarCategoriaFachada {

	private final DAOFactory daoFactory;
	private final RetirarCategoriaCasoUso casoUso;

	public RetirarCategoriaFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RetirarCategoriaCasoUsoImpl(daoFactory);
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar la categoría.", "Error técnico inesperado en RetirarCategoriaFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
