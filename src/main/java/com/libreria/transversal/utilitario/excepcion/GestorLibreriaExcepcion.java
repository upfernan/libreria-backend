package com.libreria.transversal.utilitario.excepcion;

import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;

public final class GestorLibreriaExcepcion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Throwable excepcionRaiz;
	private final String mensajeUsuario;
	private final String mensajeTecnico;

	private GestorLibreriaExcepcion(final Throwable excepcionRaiz, final String mensajeUsuario, final String mensajeTecnico) {
		super(mensajeTecnico);
		this.excepcionRaiz = UtilObjeto.obtenerValorDefecto(excepcionRaiz, new Exception());
		this.mensajeUsuario = UtilTexto.obtenerValorDefecto(mensajeUsuario);
		this.mensajeTecnico = UtilTexto.obtenerValorDefecto(mensajeTecnico);
	}

	public static GestorLibreriaExcepcion crear(final String mensajeUsuario) {
		return new GestorLibreriaExcepcion(new Exception(), mensajeUsuario, mensajeUsuario);
	}

	public static GestorLibreriaExcepcion crear(final String mensajeUsuario, final String mensajeTecnico) {
		return new GestorLibreriaExcepcion(new Exception(), mensajeUsuario, mensajeTecnico);
	}

	public static GestorLibreriaExcepcion crear(final Throwable excepcionRaiz, final String mensajeUsuario, final String mensajeTecnico) {
		return new GestorLibreriaExcepcion(excepcionRaiz, mensajeUsuario, mensajeTecnico);
	}

	public Throwable getExcepcionRaiz() {
		return excepcionRaiz;
	}

	public String getMensajeUsuario() {
		return mensajeUsuario;
	}

	public String getMensajeTecnico() {
		return mensajeTecnico;
	}

}
