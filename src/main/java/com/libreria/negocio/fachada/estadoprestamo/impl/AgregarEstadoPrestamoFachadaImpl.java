package com.libreria.negocio.fachada.estadoprestamo.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EstadoPrestamoDTO;
import com.libreria.negocio.casouso.estadoprestamo.AgregarEstadoPrestamoCasoUso;
import com.libreria.negocio.casouso.estadoprestamo.impl.AgregarEstadoPrestamoCasoUsoImpl;
import com.libreria.negocio.dominio.EstadoPrestamoDominio;
import com.libreria.negocio.fachada.estadoprestamo.AgregarEstadoPrestamoFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarEstadoPrestamoFachadaImpl implements AgregarEstadoPrestamoFachada {

	private final DAOFactory daoFactory;
	private final AgregarEstadoPrestamoCasoUso casoUso;

	public AgregarEstadoPrestamoFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new AgregarEstadoPrestamoCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final EstadoPrestamoDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final EstadoPrestamoDominio dominio = new EstadoPrestamoDominio.Builder()
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar el estado de préstamo.", "Error técnico inesperado en AgregarEstadoPrestamoFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
