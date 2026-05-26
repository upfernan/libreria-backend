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

import com.libreria.datos.dao.EstadoPrestamoDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class EstadoPrestamoSQLServerDAO extends SQLDAO implements EstadoPrestamoDAO {

	private static final Logger logger = LoggerFactory.getLogger(EstadoPrestamoSQLServerDAO.class);

	public EstadoPrestamoSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final EstadoPrestamoEntidad entidad) {
		logger.debug("Entre al metodo crear de EstadoPrestamoSQLServerDAO...");
		final String sql = "INSERT INTO estadoprestamo (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de EstadoPrestamoSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el estado de préstamo.",
					"Se presento una SQLException al ejecutar INSERT en la tabla estadoprestamo desde EstadoPrestamoSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final EstadoPrestamoEntidad entidad) {
		logger.debug("Entre al metodo actualizar de EstadoPrestamoSQLServerDAO...");
		final String sql = "UPDATE estadoprestamo SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de EstadoPrestamoSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el estado de préstamo.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla estadoprestamo desde EstadoPrestamoSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de EstadoPrestamoSQLServerDAO...");
		final String sql = "DELETE FROM estadoprestamo WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de EstadoPrestamoSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el estado de préstamo.",
					"Se presento una SQLException al ejecutar DELETE en la tabla estadoprestamo desde EstadoPrestamoSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<EstadoPrestamoEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de EstadoPrestamoSQLServerDAO...");
		final List<EstadoPrestamoEntidad> resultado = consultarPorFiltro(new EstadoPrestamoEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de EstadoPrestamoSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public EstadoPrestamoEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de EstadoPrestamoSQLServerDAO...");
		final String sql = "SELECT id, nombre, descripcion FROM estadoprestamo WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final EstadoPrestamoEntidad resultado = construirEstadoPrestamoEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de EstadoPrestamoSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de EstadoPrestamoSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el estado de préstamo por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla estadoprestamo desde EstadoPrestamoSQLServerDAO.consultarPorId.");
		}
		return null;
	}

	@Override
	public List<EstadoPrestamoEntidad> consultarPorFiltro(final EstadoPrestamoEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de EstadoPrestamoSQLServerDAO...");
		final StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion FROM estadoprestamo WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (UtilTexto.tieneContenido(filtro.getNombre())) {
				sql.append(" AND nombre = ?");
				parametros.add(filtro.getNombre());
			}
			if (UtilUUID.tieneValor(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
		}

		final List<EstadoPrestamoEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirEstadoPrestamoEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de EstadoPrestamoSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los estados de préstamo por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla estadoprestamo desde EstadoPrestamoSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private EstadoPrestamoEntidad construirEstadoPrestamoEntidad(final ResultSet rs) throws SQLException {
		return new EstadoPrestamoEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.nombre(rs.getString("nombre"))
				.descripcion(rs.getString("descripcion"))
				.build();
	}

}
