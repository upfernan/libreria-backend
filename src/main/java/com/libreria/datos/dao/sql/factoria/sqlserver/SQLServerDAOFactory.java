package com.libreria.datos.dao.sql.factoria.sqlserver;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.libreria.datos.dao.AutorDAO;
import com.libreria.datos.dao.AutorLibroDAO;
import com.libreria.datos.dao.CategoriaDAO;
import com.libreria.datos.dao.DevolucionDAO;
import com.libreria.datos.dao.EditorialDAO;
import com.libreria.datos.dao.EjemplarDAO;
import com.libreria.datos.dao.PagoDAO;
import com.libreria.datos.dao.TarifaMultaDAO;
import com.libreria.datos.dao.sql.sqlserver.AutorSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.AutorLibroSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.DevolucionSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.PagoSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.TarifaMultaSQLServerDAO;
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
import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.datos.dao.sql.sqlserver.CategoriaSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.EditorialSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.EjemplarSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.EstadoPrestamoSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.EstadoReservaSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.LibroSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.MultaSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.PrestamoSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.ReservaSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.SignaturaSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.TipoIdentificacionSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.TipoLibroSQLServerDAO;
import com.libreria.datos.dao.sql.sqlserver.UsuarioSQLServerDAO;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class SQLServerDAOFactory extends DAOFactory {

	private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=LibreriaDB;encrypt=false;trustServerCertificate=true";
	private static final String USUARIO = "sa";
	private static final String CONTRASENA = "Libreria123!";

	public SQLServerDAOFactory() {
		abrirConexion();
	}

	@Override
	protected void abrirConexion() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
			conexion.setAutoCommit(false);
		} catch (ClassNotFoundException | SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible abrir la conexión con la base de datos.");
		}
	}

	@Override
	public void cerrarConexion() {
		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible cerrar la conexión con la base de datos.");
		}
	}

	@Override
	public void iniciarTransaccion() {
		try {
			conexion.setAutoCommit(false);
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible iniciar la transacción.");
		}
	}

	@Override
	public void confirmarTransaccion() {
		try {
			conexion.commit();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible confirmar la transacción.");
		}
	}

	@Override
	public void cancelarTransaccion() {
		try {
			conexion.rollback();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible cancelar la transacción.");
		}
	}

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new UsuarioSQLServerDAO(conexion);
	}

	@Override
	public MultaDAO getMultaDAO() {
		return new MultaSQLServerDAO(conexion);
	}

	@Override
	public EjemplarDAO getEjemplarDAO() {
		return new EjemplarSQLServerDAO(conexion);
	}

	@Override
	public PrestamoDAO getPrestamoDAO() {
		return new PrestamoSQLServerDAO(conexion);
	}

	@Override
	public ReservaDAO getReservaDAO() {
		return new ReservaSQLServerDAO(conexion);
	}

	@Override
	public EstadoPrestamoDAO getEstadoPrestamoDAO() {
		return new EstadoPrestamoSQLServerDAO(conexion);
	}

	@Override
	public EstadoReservaDAO getEstadoReservaDAO() {
		return new EstadoReservaSQLServerDAO(conexion);
	}

	@Override
	public TipoIdentificacionDAO getTipoIdentificacionDAO() {
		return new TipoIdentificacionSQLServerDAO(conexion);
	}

	@Override
	public TipoLibroDAO getTipoLibroDAO() {
		return new TipoLibroSQLServerDAO(conexion);
	}

	@Override
	public CategoriaDAO getCategoriaDAO() {
		return new CategoriaSQLServerDAO(conexion);
	}

	@Override
	public EditorialDAO getEditorialDAO() {
		return new EditorialSQLServerDAO(conexion);
	}

	@Override
	public LibroDAO getLibroDAO() {
		return new LibroSQLServerDAO(conexion);
	}

	@Override
	public SignaturaDAO getSignaturaDAO() {
		return new SignaturaSQLServerDAO(conexion);
	}

	@Override
	public AutorDAO getAutorDAO() {
		return new AutorSQLServerDAO(conexion);
	}

	@Override
	public AutorLibroDAO getAutorLibroDAO() {
		return new AutorLibroSQLServerDAO(conexion);
	}

	@Override
	public DevolucionDAO getDevolucionDAO() {
		return new DevolucionSQLServerDAO(conexion);
	}

	@Override
	public PagoDAO getPagoDAO() {
		return new PagoSQLServerDAO(conexion);
	}

	@Override
	public TarifaMultaDAO getTarifaMultaDAO() {
		return new TarifaMultaSQLServerDAO(conexion);
	}

}
