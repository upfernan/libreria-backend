package com.libreria.negocio.fachada.reserva.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.ReservaDTO;
import com.libreria.negocio.casouso.reserva.RegistrarReservaCasoUso;
import com.libreria.negocio.casouso.reserva.impl.RegistrarReservaCasoUsoImpl;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.negocio.dominio.ReservaDominio;
import com.libreria.negocio.dominio.UsuarioDominio;
import com.libreria.negocio.fachada.reserva.RegistrarReservaFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarReservaFachadaImpl implements RegistrarReservaFachada {

	private DAOFactory daoFactory;
	private RegistrarReservaCasoUso casoUso;

	public RegistrarReservaFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RegistrarReservaCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final ReservaDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final ReservaDominio dominio = new ReservaDominio.Builder()
					.usuario(new UsuarioDominio.Builder()
							.id(datos.getUsuario().getId())
							.build())
					.libro(new LibroDominio.Builder()
							.id(datos.getLibro().getId())
							.build())
					.build();

			casoUso.ejecutar(dominio);

			daoFactory.confirmarTransaccion();

		} catch (GestorLibreriaExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (Exception excepcion) {
			daoFactory.cancelarTransaccion();
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar la reserva.", "Error técnico inesperado en RegistrarReservaFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
