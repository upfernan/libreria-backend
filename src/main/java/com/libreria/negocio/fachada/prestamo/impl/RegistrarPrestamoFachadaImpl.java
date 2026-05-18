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
	public static void main(final String[] args) {


	 
	    final java.util.UUID usuarioId  =
	            java.util.UUID.fromString("00000000-0000-0000-0000-000000000000");
	    final java.util.UUID ejemplarId =
	            java.util.UUID.fromString("00000000-0000-0000-0000-000000000000");

	    final com.libreria.dto.UsuarioDTO usuario =
	            new com.libreria.dto.UsuarioDTO.Builder()
	                    .id(usuarioId)
	                    .build();

	    final com.libreria.dto.EjemplarDTO ejemplar =
	            new com.libreria.dto.EjemplarDTO.Builder()
	                    .id(ejemplarId)
	                    .build();

	    final com.libreria.dto.PrestamoDTO datos =
	            new com.libreria.dto.PrestamoDTO.Builder()
	                    .usuario(usuario)
	                    .ejemplar(ejemplar)
	                    .build();

	    try {
	        new RegistrarPrestamoFachadaImpl().ejecutar(datos);
	        System.out.println("Préstamo registrado exitosamente.");
	    } catch (Exception e) {
	        System.err.println("Error: " + e.getMessage());
	    }
	}
	

}
