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

import com.libreria.datos.dao.TipoLibroDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class TipoLibroSQLServerDAO extends SQLDAO implements TipoLibroDAO {

	private static final Logger logger = LoggerFactory.getLogger(TipoLibroSQLServerDAO.class);

	public TipoLibroSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final TipoLibroEntidad entidad) {
		logger.debug("Entre al metodo crear de TipoLibroSQLServerDAO...");
		final String sql = "INSERT INTO tipolibro (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de TipoLibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el tipo de libro.",
					"Se presento una SQLException al ejecutar INSERT en la tabla tipolibro desde TipoLibroSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final TipoLibroEntidad entidad) {
		logger.debug("Entre al metodo actualizar de TipoLibroSQLServerDAO...");
		final String sql = "UPDATE tipolibro SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de TipoLibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el tipo de libro.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla tipolibro desde TipoLibroSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de TipoLibroSQLServerDAO...");
		final String sql = "DELETE FROM tipolibro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de TipoLibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el tipo de libro.",
					"Se presento una SQLException al ejecutar DELETE en la tabla tipolibro desde TipoLibroSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<TipoLibroEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de TipoLibroSQLServerDAO...");
		final List<TipoLibroEntidad> resultado = consultarPorFiltro(new TipoLibroEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de TipoLibroSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public TipoLibroEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de TipoLibroSQLServerDAO...");
		final String sql = "SELECT id, nombre, descripcion FROM tipolibro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final TipoLibroEntidad resultado = construirTipoLibroEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de TipoLibroSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de TipoLibroSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el tipo de libro por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla tipolibro desde TipoLibroSQLServerDAO.consultarPorId.");
		}
		return null;
	}

	@Override
	public List<TipoLibroEntidad> consultarPorFiltro(final TipoLibroEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de TipoLibroSQLServerDAO...");
		final StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion FROM tipolibro WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (UtilUUID.tieneValor(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (UtilTexto.tieneContenido(filtro.getNombre())) {
				sql.append(" AND nombre = ?");
				parametros.add(filtro.getNombre());
			}
		}

		final List<TipoLibroEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirTipoLibroEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de TipoLibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los tipos de libro por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla tipolibro desde TipoLibroSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private TipoLibroEntidad construirTipoLibroEntidad(final ResultSet rs) throws SQLException {
		return new TipoLibroEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.nombre(rs.getString("nombre"))
				.descripcion(rs.getString("descripcion"))
				.build();
	}

}
