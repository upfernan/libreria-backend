package com.libreria.negocio.fachada.estadoprestamo.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.estadoprestamo.RetirarEstadoPrestamoCasoUso;
import com.libreria.negocio.casouso.estadoprestamo.impl.RetirarEstadoPrestamoCasoUsoImpl;
import com.libreria.negocio.fachada.estadoprestamo.RetirarEstadoPrestamoFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEstadoPrestamoFachadaImpl implements RetirarEstadoPrestamoFachada {

	private final DAOFactory daoFactory;
	private final RetirarEstadoPrestamoCasoUso casoUso;

	public RetirarEstadoPrestamoFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RetirarEstadoPrestamoCasoUsoImpl(daoFactory);
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar el estado de préstamo.", "Error técnico inesperado en RetirarEstadoPrestamoFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
