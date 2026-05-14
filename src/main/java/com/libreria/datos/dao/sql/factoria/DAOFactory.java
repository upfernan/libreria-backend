package com.libreria.datos.dao.sql.factoria;

import java.sql.Connection;

import com.libreria.datos.dao.CategoriaDAO;
import com.libreria.datos.dao.EditorialDAO;
import com.libreria.datos.dao.EjemplarDAO;
import com.libreria.datos.dao.EstadoPrestamoDAO;
import com.libreria.datos.dao.EstadoReservaDAO;
import com.libreria.datos.dao.LibroDAO;
import com.libreria.datos.dao.MultaDAO;
import com.libreria.datos.dao.PrestamoDAO;
import com.libreria.datos.dao.ReservaDAO;
import com.libreria.datos.dao.SignaturaDAO;
import com.libreria.datos.dao.TipoIdentificacionDAO;
import com.libreria.datos.dao.TipoLibroDAO;
import com.libreria.datos.dao.UsuarioDAO;
import com.libreria.datos.dao.sql.factoria.sqlserver.SQLServerDAOFactory;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public abstract class DAOFactory {

	protected Connection conexion;

	private static final TipoFactoriaEnum FACTORIA_ACTUAL = TipoFactoriaEnum.SQLSERVER;

	public static DAOFactory getFactory() {
		switch (FACTORIA_ACTUAL) {
		case SQLSERVER:
			return new SQLServerDAOFactory();
		default:
			throw GestorLibreriaExcepcion.crear("Tipo de factoría no soportada: " + FACTORIA_ACTUAL);
		}
	}

	protected abstract void abrirConexion();

	public abstract void cerrarConexion();

	public abstract void iniciarTransaccion();

	public abstract void confirmarTransaccion();

	public abstract void cancelarTransaccion();

	public abstract UsuarioDAO getUsuarioDAO();

	public abstract MultaDAO getMultaDAO();

	public abstract EjemplarDAO getEjemplarDAO();

	public abstract PrestamoDAO getPrestamoDAO();

	public abstract ReservaDAO getReservaDAO();

	public abstract EstadoPrestamoDAO getEstadoPrestamoDAO();

	public abstract EstadoReservaDAO getEstadoReservaDAO();

	public abstract TipoIdentificacionDAO getTipoIdentificacionDAO();

	public abstract TipoLibroDAO getTipoLibroDAO();

	public abstract CategoriaDAO getCategoriaDAO();

	public abstract EditorialDAO getEditorialDAO();

	public abstract LibroDAO getLibroDAO();

	public abstract SignaturaDAO getSignaturaDAO();

}
