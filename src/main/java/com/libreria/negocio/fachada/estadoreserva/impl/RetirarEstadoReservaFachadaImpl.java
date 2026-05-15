package com.libreria.negocio.fachada.estadoreserva.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.estadoreserva.RetirarEstadoReservaCasoUso;
import com.libreria.negocio.casouso.estadoreserva.impl.RetirarEstadoReservaCasoUsoImpl;
import com.libreria.negocio.fachada.estadoreserva.RetirarEstadoReservaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEstadoReservaFachadaImpl implements RetirarEstadoReservaFachada {

	private final DAOFactory daoFactory;
	private final RetirarEstadoReservaCasoUso casoUso;

	public RetirarEstadoReservaFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RetirarEstadoReservaCasoUsoImpl(daoFactory);
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar el estado de reserva.", "Error técnico inesperado en RetirarEstadoReservaFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
