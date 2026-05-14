package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.MultaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class MultaSQLServerDAO extends SQLDAO implements MultaDAO {

	public MultaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final MultaEntidad entidad) {
		final String sql = "INSERT INTO multa (id, montoTotal, fechaGeneracion, pagada, diasRetraso, tarifaMultaId, devolucionId, usuarioAfectadoId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setDouble(2, entidad.getMontoTotal());
			ps.setDate(3, java.sql.Date.valueOf(entidad.getFechaGeneracion()));
			ps.setBoolean(4, entidad.getPagada());
			ps.setInt(5, entidad.getDiasRetraso());
			ps.setString(6, entidad.getTarifaMulta().getId().toString());
			ps.setString(7, entidad.getDevolucion().getId().toString());
			ps.setString(8, entidad.getUsuarioAfectado().getId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar la multa.");
		}
	}

	@Override
	public void actualizar(final UUID id, final MultaEntidad entidad) {
		final String sql = "UPDATE multa SET pagada = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setBoolean(1, entidad.getPagada());
			ps.setString(2, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar la multa.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM multa WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar la multa.");
		}
	}

	@Override
	public List<MultaEntidad> consultarTodos() {
		final String sql = "SELECT id, montoTotal, fechaGeneracion, pagada, diasRetraso, tarifaMultaId, devolucionId, usuarioAfectadoId FROM multa";
		final List<MultaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirMultaEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar las multas.");
		}
		return resultados;
	}

	@Override
	public MultaEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, montoTotal, fechaGeneracion, pagada, diasRetraso, tarifaMultaId, devolucionId, usuarioAfectadoId FROM multa WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirMultaEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar la multa por identificador.");
		}
		return null;
	}

	@Override
	public List<MultaEntidad> consultarPorFiltro(final MultaEntidad filtro) {
		final StringBuilder sql = new StringBuilder(
				"SELECT id, montoTotal, fechaGeneracion, pagada, diasRetraso, tarifaMultaId, devolucionId, usuarioAfectadoId FROM multa WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getUsuarioAfectado()) && !UtilObjeto.esNulo(filtro.getUsuarioAfectado().getId())) {
				sql.append(" AND usuarioAfectadoId = ?");
				parametros.add(filtro.getUsuarioAfectado().getId().toString());
				sql.append(" AND pagada = ?");
				parametros.add(filtro.getPagada());
			}
			if (!UtilObjeto.esNulo(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
		}

		final List<MultaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirMultaEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar las multas por filtro.");
		}
		return resultados;
	}

	private MultaEntidad construirMultaEntidad(final ResultSet rs) throws SQLException {
		return new MultaEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.montoTotal(rs.getDouble("montoTotal"))
				.fechaGeneracion(rs.getDate("fechaGeneracion").toLocalDate())
				.pagada(rs.getBoolean("pagada"))
				.diasRetraso(rs.getInt("diasRetraso"))
				.tarifaMulta(new TarifaMultaEntidad.Builder()
						.id(UUID.fromString(rs.getString("tarifaMultaId")))
						.build())
				.devolucion(new DevolucionEntidad.Builder()
						.id(UUID.fromString(rs.getString("devolucionId")))
						.build())
				.usuarioAfectado(new UsuarioEntidad.Builder()
						.id(UUID.fromString(rs.getString("usuarioAfectadoId")))
						.build())
				.build();
	}

}
