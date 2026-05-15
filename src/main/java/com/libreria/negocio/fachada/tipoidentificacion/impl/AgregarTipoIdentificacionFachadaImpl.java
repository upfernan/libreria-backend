package com.libreria.negocio.fachada.tipoidentificacion.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoIdentificacionDTO;
import com.libreria.negocio.casouso.tipoidentificacion.AgregarTipoIdentificacionCasoUso;
import com.libreria.negocio.casouso.tipoidentificacion.impl.AgregarTipoIdentificacionCasoUsoImpl;
import com.libreria.negocio.dominio.TipoIdentificacionDominio;
import com.libreria.negocio.fachada.tipoidentificacion.AgregarTipoIdentificacionFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarTipoIdentificacionFachadaImpl implements AgregarTipoIdentificacionFachada {

	private final DAOFactory daoFactory;
	private final AgregarTipoIdentificacionCasoUso casoUso;

	public AgregarTipoIdentificacionFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new AgregarTipoIdentificacionCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final TipoIdentificacionDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final TipoIdentificacionDominio dominio = new TipoIdentificacionDominio.Builder()
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
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al agregar el tipo de identificación.", "Error técnico inesperado en AgregarTipoIdentificacionFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
