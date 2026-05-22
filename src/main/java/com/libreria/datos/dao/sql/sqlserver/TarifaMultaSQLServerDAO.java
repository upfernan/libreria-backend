package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.TarifaMultaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class TarifaMultaSQLServerDAO extends SQLDAO implements TarifaMultaDAO {

	public TarifaMultaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final TarifaMultaEntidad entidad) {
		final String sql = "INSERT INTO tarifamulta (id, valorDiario, fechaInicioVigencia, fechaFinVigencia) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setDouble(2, entidad.getValorDiario());
			ps.setDate(3, java.sql.Date.valueOf(entidad.getFechaInicioVigencia()));
			ps.setDate(4, java.sql.Date.valueOf(entidad.getFechaFinVigencia()));
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar la tarifa de multa.");
		}
	}

	@Override
	public void actualizar(final UUID id, final TarifaMultaEntidad entidad) {
		final String sql = "UPDATE tarifamulta SET valorDiario = ?, fechaInicioVigencia = ?, fechaFinVigencia = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setDouble(1, entidad.getValorDiario());
			ps.setDate(2, java.sql.Date.valueOf(entidad.getFechaInicioVigencia()));
			ps.setDate(3, java.sql.Date.valueOf(entidad.getFechaFinVigencia()));
			ps.setString(4, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar la tarifa de multa.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM tarifamulta WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar la tarifa de multa.");
		}
	}

	@Override
	public List<TarifaMultaEntidad> consultarTodos() {
		final String sql = "SELECT id, valorDiario, fechaInicioVigencia, fechaFinVigencia FROM tarifamulta ORDER BY fechaInicioVigencia DESC";
		final List<TarifaMultaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirTarifaMultaEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las tarifas de multa.");
		}
		return resultados;
	}

	@Override
	public TarifaMultaEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, valorDiario, fechaInicioVigencia, fechaFinVigencia FROM tarifamulta WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirTarifaMultaEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar la tarifa de multa por identificador.");
		}
		return null;
	}

	@Override
	public List<TarifaMultaEntidad> consultarPorFiltro(final TarifaMultaEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, valorDiario, fechaInicioVigencia, fechaFinVigencia FROM tarifamulta WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilFecha.esNula(filtro.getFechaInicioVigencia()) && !filtro.getFechaInicioVigencia().equals(UtilFecha.FECHA_DEFECTO)) {
				sql.append(" AND fechaInicioVigencia = ?");
				parametros.add(java.sql.Date.valueOf(filtro.getFechaInicioVigencia()));
			}
			if (!UtilFecha.esNula(filtro.getFechaFinVigencia()) && !filtro.getFechaFinVigencia().equals(UtilFecha.FECHA_DEFECTO)) {
				sql.append(" AND fechaFinVigencia = ?");
				parametros.add(java.sql.Date.valueOf(filtro.getFechaFinVigencia()));
			}
		}

		final List<TarifaMultaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirTarifaMultaEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las tarifas de multa por filtro.");
		}
		return resultados;
	}

	private TarifaMultaEntidad construirTarifaMultaEntidad(final ResultSet rs) throws SQLException {
		return new TarifaMultaEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.valorDiario(rs.getDouble("valorDiario"))
				.fechaInicioVigencia(rs.getDate("fechaInicioVigencia").toLocalDate())
				.fechaFinVigencia(rs.getDate("fechaFinVigencia").toLocalDate())
				.build();
	}

}
