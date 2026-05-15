package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.DevolucionDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class DevolucionSQLServerDAO extends SQLDAO implements DevolucionDAO {

	public DevolucionSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final DevolucionEntidad entidad) {
		final String sql = "INSERT INTO devolucion (id, fechaDevolucion, prestamoId) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setDate(2, java.sql.Date.valueOf(entidad.getFechaDevolucion()));
			ps.setString(3, entidad.getPrestamo().getId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar la devolución.");
		}
	}

	@Override
	public void actualizar(final UUID id, final DevolucionEntidad entidad) {
		final String sql = "UPDATE devolucion SET fechaDevolucion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setDate(1, java.sql.Date.valueOf(entidad.getFechaDevolucion()));
			ps.setString(2, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar la devolución.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM devolucion WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar la devolución.");
		}
	}

	@Override
	public List<DevolucionEntidad> consultarTodos() {
		final String sql = "SELECT id, fechaDevolucion, prestamoId FROM devolucion";
		final List<DevolucionEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirDevolucionEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar las devoluciones.");
		}
		return resultados;
	}

	@Override
	public DevolucionEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, fechaDevolucion, prestamoId FROM devolucion WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirDevolucionEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar la devolución por identificador.");
		}
		return null;
	}

	@Override
	public List<DevolucionEntidad> consultarPorFiltro(final DevolucionEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, fechaDevolucion, prestamoId FROM devolucion WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getPrestamo()) && !UtilObjeto.esNulo(filtro.getPrestamo().getId())) {
				sql.append(" AND prestamoId = ?");
				parametros.add(filtro.getPrestamo().getId().toString());
			}
		}

		final List<DevolucionEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirDevolucionEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar las devoluciones por filtro.");
		}
		return resultados;
	}

	private DevolucionEntidad construirDevolucionEntidad(final ResultSet rs) throws SQLException {
		return new DevolucionEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.fechaDevolucion(rs.getDate("fechaDevolucion").toLocalDate())
				.prestamo(new PrestamoEntidad.Builder()
						.id(UUID.fromString(rs.getString("prestamoId")))
						.build())
				.build();
	}

}
