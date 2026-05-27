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

import com.libreria.datos.dao.AutorLibroDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.AutorEntidad;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AutorLibroSQLServerDAO extends SQLDAO implements AutorLibroDAO {

	private static final Logger logger = LoggerFactory.getLogger(AutorLibroSQLServerDAO.class);

	public AutorLibroSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final AutorLibroEntidad entidad) {
		logger.debug("Entre al metodo crear de AutorLibroSQLServerDAO...");
		final String sql = "INSERT INTO autorlibro (id, autorId, libroId) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getAutor().getId().toString());
			ps.setString(3, entidad.getLibro().getId().toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de AutorLibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el autor-libro.",
					"Se presento una SQLException al ejecutar INSERT en la tabla autorlibro desde AutorLibroSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final AutorLibroEntidad entidad) {
		logger.debug("Entre al metodo actualizar de AutorLibroSQLServerDAO...");
		final String sql = "UPDATE autorlibro SET autorId = ?, libroId = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getAutor().getId().toString());
			ps.setString(2, entidad.getLibro().getId().toString());
			ps.setString(3, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de AutorLibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el autor-libro.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla autorlibro desde AutorLibroSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de AutorLibroSQLServerDAO...");
		final String sql = "DELETE FROM autorlibro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de AutorLibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el autor-libro.",
					"Se presento una SQLException al ejecutar DELETE en la tabla autorlibro desde AutorLibroSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<AutorLibroEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de AutorLibroSQLServerDAO...");
		final List<AutorLibroEntidad> resultado = consultarPorFiltro(new AutorLibroEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de AutorLibroSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public AutorLibroEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de AutorLibroSQLServerDAO...");
		final String sql = "SELECT id, autorId, libroId FROM autorlibro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final AutorLibroEntidad resultado = construirAutorLibroEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de AutorLibroSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de AutorLibroSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el autor-libro por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla autorlibro desde AutorLibroSQLServerDAO.consultarPorId.");
		}
		return new AutorLibroEntidad.Builder().build();
	}

	@Override
	public List<AutorLibroEntidad> consultarPorFiltro(final AutorLibroEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de AutorLibroSQLServerDAO...");
		final StringBuilder sql = new StringBuilder("SELECT id, autorId, libroId FROM autorlibro WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getAutor()) && UtilUUID.tieneValor(filtro.getAutor().getId())) {
				sql.append(" AND autorId = ?");
				parametros.add(filtro.getAutor().getId().toString());
			}
			if (!UtilObjeto.esNulo(filtro.getLibro()) && UtilUUID.tieneValor(filtro.getLibro().getId())) {
				sql.append(" AND libroId = ?");
				parametros.add(filtro.getLibro().getId().toString());
			}
		}

		final List<AutorLibroEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirAutorLibroEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de AutorLibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los autores-libro por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla autorlibro desde AutorLibroSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private AutorLibroEntidad construirAutorLibroEntidad(final ResultSet rs) throws SQLException {
		return new AutorLibroEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.autor(new AutorEntidad.Builder()
						.id(UUID.fromString(rs.getString("autorId")))
						.build())
				.libro(new LibroEntidad.Builder()
						.id(UUID.fromString(rs.getString("libroId")))
						.build())
				.build();
	}

}
