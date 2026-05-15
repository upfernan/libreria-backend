package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.PagoDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PagoEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class PagoSQLServerDAO extends SQLDAO implements PagoDAO {

	public PagoSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final PagoEntidad entidad) {
		final String sql = "INSERT INTO pago (id, fechaPago, multaId) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setDate(2, java.sql.Date.valueOf(entidad.getFechaPago()));
			ps.setString(3, entidad.getMulta().getId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar el pago.");
		}
	}

	@Override
	public void actualizar(final UUID id, final PagoEntidad entidad) {
		final String sql = "UPDATE pago SET fechaPago = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setDate(1, java.sql.Date.valueOf(entidad.getFechaPago()));
			ps.setString(2, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar el pago.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM pago WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar el pago.");
		}
	}

	@Override
	public List<PagoEntidad> consultarTodos() {
		final String sql = "SELECT id, fechaPago, multaId FROM pago";
		final List<PagoEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirPagoEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los pagos.");
		}
		return resultados;
	}

	@Override
	public PagoEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, fechaPago, multaId FROM pago WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirPagoEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar el pago por identificador.");
		}
		return null;
	}

	@Override
	public List<PagoEntidad> consultarPorFiltro(final PagoEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, fechaPago, multaId FROM pago WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getMulta()) && !UtilObjeto.esNulo(filtro.getMulta().getId())) {
				sql.append(" AND multaId = ?");
				parametros.add(filtro.getMulta().getId().toString());
			}
		}

		final List<PagoEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirPagoEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los pagos por filtro.");
		}
		return resultados;
	}

	private PagoEntidad construirPagoEntidad(final ResultSet rs) throws SQLException {
		return new PagoEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.fechaPago(rs.getDate("fechaPago").toLocalDate())
				.multa(new MultaEntidad.Builder()
						.id(UUID.fromString(rs.getString("multaId")))
						.build())
				.build();
	}

}
