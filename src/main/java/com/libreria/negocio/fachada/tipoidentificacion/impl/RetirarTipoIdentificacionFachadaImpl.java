package com.libreria.negocio.fachada.tipoidentificacion.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.negocio.casouso.tipoidentificacion.RetirarTipoIdentificacionCasoUso;
import com.libreria.negocio.casouso.tipoidentificacion.impl.RetirarTipoIdentificacionCasoUsoImpl;
import com.libreria.negocio.fachada.tipoidentificacion.RetirarTipoIdentificacionFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarTipoIdentificacionFachadaImpl implements RetirarTipoIdentificacionFachada {

	private final DAOFactory daoFactory;
	private final RetirarTipoIdentificacionCasoUso casoUso;

	public RetirarTipoIdentificacionFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RetirarTipoIdentificacionCasoUsoImpl(daoFactory);
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al retirar el tipo de identificación.", "Error técnico inesperado en RetirarTipoIdentificacionFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
