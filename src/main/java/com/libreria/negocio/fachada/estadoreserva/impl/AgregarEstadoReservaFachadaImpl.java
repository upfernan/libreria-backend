package com.libreria.negocio.fachada.estadoreserva.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EstadoReservaDTO;
import com.libreria.negocio.casouso.estadoreserva.AgregarEstadoReservaCasoUso;
import com.libreria.negocio.casouso.estadoreserva.impl.AgregarEstadoReservaCasoUsoImpl;
import com.libreria.negocio.dominio.EstadoReservaDominio;
import com.libreria.negocio.fachada.estadoreserva.AgregarEstadoReservaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarEstadoReservaFachadaImpl implements AgregarEstadoReservaFachada {

	private final DAOFactory daoFactory;
	private final AgregarEstadoReservaCasoUso casoUso;

	public AgregarEstadoReservaFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new AgregarEstadoReservaCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final EstadoReservaDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final EstadoReservaDominio dominio = new EstadoReservaDominio.Builder()
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar el estado de reserva.", "Error técnico inesperado en AgregarEstadoReservaFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
