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

import com.libreria.datos.dao.TipoIdentificacionDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class TipoIdentificacionSQLServerDAO extends SQLDAO implements TipoIdentificacionDAO {

	private static final Logger logger = LoggerFactory.getLogger(TipoIdentificacionSQLServerDAO.class);

	public TipoIdentificacionSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final TipoIdentificacionEntidad entidad) {
		logger.debug("Entre al metodo crear de TipoIdentificacionSQLServerDAO...");
		final String sql = "INSERT INTO tipoidentificacion (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de TipoIdentificacionSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el tipo de identificación.",
					"Se presento una SQLException al ejecutar INSERT en la tabla tipoidentificacion desde TipoIdentificacionSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final TipoIdentificacionEntidad entidad) {
		logger.debug("Entre al metodo actualizar de TipoIdentificacionSQLServerDAO...");
		final String sql = "UPDATE tipoidentificacion SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de TipoIdentificacionSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el tipo de identificación.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla tipoidentificacion desde TipoIdentificacionSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de TipoIdentificacionSQLServerDAO...");
		final String sql = "DELETE FROM tipoidentificacion WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de TipoIdentificacionSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el tipo de identificación.",
					"Se presento una SQLException al ejecutar DELETE en la tabla tipoidentificacion desde TipoIdentificacionSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<TipoIdentificacionEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de TipoIdentificacionSQLServerDAO...");
		final List<TipoIdentificacionEntidad> resultado = consultarPorFiltro(new TipoIdentificacionEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de TipoIdentificacionSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public TipoIdentificacionEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de TipoIdentificacionSQLServerDAO...");
		final String sql = "SELECT id, nombre, descripcion FROM tipoidentificacion WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final TipoIdentificacionEntidad resultado = construirTipoIdentificacionEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de TipoIdentificacionSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de TipoIdentificacionSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el tipo de identificación por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla tipoidentificacion desde TipoIdentificacionSQLServerDAO.consultarPorId.");
		}
		return new TipoIdentificacionEntidad.Builder().build();
	}

	@Override
	public List<TipoIdentificacionEntidad> consultarPorFiltro(final TipoIdentificacionEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de TipoIdentificacionSQLServerDAO...");
		final StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion FROM tipoidentificacion WHERE 1=1");
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

		final List<TipoIdentificacionEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirTipoIdentificacionEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de TipoIdentificacionSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los tipos de identificación por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla tipoidentificacion desde TipoIdentificacionSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private TipoIdentificacionEntidad construirTipoIdentificacionEntidad(final ResultSet rs) throws SQLException {
		return new TipoIdentificacionEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.nombre(rs.getString("nombre"))
				.descripcion(rs.getString("descripcion"))
				.build();
	}

}
