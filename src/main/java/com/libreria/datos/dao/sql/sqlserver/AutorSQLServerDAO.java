package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.libreria.datos.dao.AutorDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.AutorEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AutorSQLServerDAO extends SQLDAO implements AutorDAO {

	private static final Logger logger = LoggerFactory.getLogger(AutorSQLServerDAO.class);

	public AutorSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final AutorEntidad entidad) {
		logger.debug("Entre al metodo crear de AutorSQLServerDAO...");
		final String sql = "INSERT INTO autor (id, primerNombre, segundoNombre, primerApellido, segundoApellido) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getPrimerNombre());
			ps.setString(3, entidad.getSegundoNombre());
			ps.setString(4, entidad.getPrimerApellido());
			ps.setString(5, entidad.getSegundoApellido());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de AutorSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el autor.",
					"Se presento una SQLException al ejecutar INSERT en la tabla autor desde AutorSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final AutorEntidad entidad) {
		logger.debug("Entre al metodo actualizar de AutorSQLServerDAO...");
		final String sql = "UPDATE autor SET primerNombre = ?, segundoNombre = ?, primerApellido = ?, segundoApellido = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getPrimerNombre());
			ps.setString(2, entidad.getSegundoNombre());
			ps.setString(3, entidad.getPrimerApellido());
			ps.setString(4, entidad.getSegundoApellido());
			ps.setString(5, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de AutorSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el autor.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla autor desde AutorSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de AutorSQLServerDAO...");
		final String sql = "DELETE FROM autor WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de AutorSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el autor.",
					"Se presento una SQLException al ejecutar DELETE en la tabla autor desde AutorSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<AutorEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de AutorSQLServerDAO...");
		final List<AutorEntidad> resultado = consultarPorFiltro(new AutorEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de AutorSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public AutorEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de AutorSQLServerDAO...");
		final String sql = "SELECT id, primerNombre, segundoNombre, primerApellido, segundoApellido FROM autor WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final AutorEntidad resultado = construirAutorEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de AutorSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de AutorSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el autor por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla autor desde AutorSQLServerDAO.consultarPorId.");
		}
		return null;
	}

	@Override
	public List<AutorEntidad> consultarPorFiltro(final AutorEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de AutorSQLServerDAO...");
		final StringBuilder sql = new StringBuilder("SELECT id, primerNombre, segundoNombre, primerApellido, segundoApellido FROM autor WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (UtilTexto.tieneContenido(filtro.getPrimerNombre())) {
				sql.append(" AND primerNombre = ?");
				parametros.add(filtro.getPrimerNombre());
			}
			if (UtilTexto.tieneContenido(filtro.getPrimerApellido())) {
				sql.append(" AND primerApellido = ?");
				parametros.add(filtro.getPrimerApellido());
			}
		}

		final List<AutorEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirAutorEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de AutorSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los autores por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla autor desde AutorSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private AutorEntidad construirAutorEntidad(final ResultSet rs) throws SQLException {
		return new AutorEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.primerNombre(rs.getString("primerNombre"))
				.segundoNombre(rs.getString("segundoNombre"))
				.primerApellido(rs.getString("primerApellido"))
				.segundoApellido(rs.getString("segundoApellido"))
				.build();
	}

}
