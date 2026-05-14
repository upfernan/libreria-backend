package com.libreria.negocio.fachada.usuario.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.UsuarioDTO;
import com.libreria.negocio.casouso.usuario.RegistrarUsuarioCasoUso;
import com.libreria.negocio.casouso.usuario.impl.RegistrarUsuarioCasoUsoImpl;
import com.libreria.negocio.dominio.TipoIdentificacionDominio;
import com.libreria.negocio.dominio.UsuarioDominio;
import com.libreria.negocio.fachada.usuario.RegistrarUsuarioFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RegistrarUsuarioFachadaImpl implements RegistrarUsuarioFachada {

	private DAOFactory daoFactory;
	private RegistrarUsuarioCasoUso casoUso;

	public RegistrarUsuarioFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new RegistrarUsuarioCasoUsoImpl(daoFactory);
	}

	@Override
	public void ejecutar(final UsuarioDTO datos) {
		try {
			daoFactory.iniciarTransaccion();

			final UsuarioDominio dominio = new UsuarioDominio.Builder()
					.tipoIdentificacion(new TipoIdentificacionDominio.Builder()
							.id(datos.getTipoIdentificacion().getId())
							.build())
					.numeroIdentificacion(datos.getNumeroIdentificacion())
					.primerNombre(datos.getPrimerNombre())
					.segundoNombre(datos.getSegundoNombre())
					.primerApellido(datos.getPrimerApellido())
					.segundoApellido(datos.getSegundoApellido())
					.correoElectronico(datos.getCorreoElectronico())
					.build();

			casoUso.ejecutar(dominio);

			daoFactory.confirmarTransaccion();

		} catch (GestorLibreriaExcepcion excepcion) {
			daoFactory.cancelarTransaccion();
			throw excepcion;

		} catch (Exception excepcion) {
			daoFactory.cancelarTransaccion();
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar el usuario.", "Error técnico inesperado en RegistrarUsuarioFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
