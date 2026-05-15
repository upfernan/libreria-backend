package com.libreria.negocio.fachada.estadoreserva.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EstadoReservaDTO;
import com.libreria.negocio.casouso.estadoreserva.ActualizarEstadoReservaCasoUso;
import com.libreria.negocio.casouso.estadoreserva.impl.ActualizarEstadoReservaCasoUsoImpl;
import com.libreria.negocio.dominio.EstadoReservaDominio;
import com.libreria.negocio.fachada.estadoreserva.ActualizarEstadoReservaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEstadoReservaFachadaImpl implements ActualizarEstadoReservaFachada {

	private final DAOFactory daoFactory;
	private final ActualizarEstadoReservaCasoUso casoUso;

	public ActualizarEstadoReservaFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ActualizarEstadoReservaCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final EstadoReservaDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final EstadoReservaDominio dominio = new EstadoReservaDominio.Builder()
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el estado de reserva.", "Error técnico inesperado en ActualizarEstadoReservaFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
