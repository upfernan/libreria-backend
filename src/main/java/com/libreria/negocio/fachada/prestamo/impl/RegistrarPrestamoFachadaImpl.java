package com.libreria.negocio.fachada.prestamo.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.PrestamoDTO;
import com.libreria.negocio.casouso.prestamo.RegistrarPrestamoCasoUso;
import com.libreria.negocio.casouso.prestamo.impl.RegistrarPrestamoCasoUsoImpl;
import com.libreria.negocio.dominio.EjemplarDominio;
import com.libreria.negocio.dominio.PrestamoDominio;
import com.libreria.negocio.dominio.UsuarioDominio;
import com.libreria.negocio.fachada.prestamo.RegistrarPrestamoFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarPrestamoFachadaImpl implements RegistrarPrestamoFachada {

	private DAOFactory daoFactory;
	private RegistrarPrestamoCasoUso casoUso;

	public RegistrarPrestamoFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RegistrarPrestamoCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final PrestamoDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final PrestamoDominio dominio = new PrestamoDominio.Builder()
					.usuario(new UsuarioDominio.Builder()
							.id(datos.getUsuario().getId())
							.build())
					.ejemplar(new EjemplarDominio.Builder()
							.id(datos.getEjemplar().getId())
							.build())
					.build();

			casoUso.ejecutar(dominio);

			daoFactory.confirmarTransaccion();

		} catch (GestorLibreriaExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (Exception excepcion) {
			daoFactory.cancelarTransaccion();
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar el préstamo.", "Error técnico inesperado en RegistrarPrestamoFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
