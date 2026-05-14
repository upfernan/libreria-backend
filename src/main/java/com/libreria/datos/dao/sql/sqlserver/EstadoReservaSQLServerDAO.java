package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.EstadoReservaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class EstadoReservaSQLServerDAO extends SQLDAO implements EstadoReservaDAO {

	public EstadoReservaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final EstadoReservaEntidad entidad) {
		final String sql = "INSERT INTO estadoreserva (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar el estado de reserva.");
		}
	}

	@Override
	public void actualizar(final UUID id, final EstadoReservaEntidad entidad) {
		final String sql = "UPDATE estadoreserva SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar el estado de reserva.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM estadoreserva WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar el estado de reserva.");
		}
	}

	@Override
	public List<EstadoReservaEntidad> consultarTodos() {
		final String sql = "SELECT id, nombre, descripcion FROM estadoreserva";
		final List<EstadoReservaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirEstadoReservaEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los estados de reserva.");
		}
		return resultados;
	}

	@Override
	public EstadoReservaEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, nombre, descripcion FROM estadoreserva WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirEstadoReservaEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar el estado de reserva por identificador.");
		}
		return null;
	}

	@Override
	public List<EstadoReservaEntidad> consultarPorFiltro(final EstadoReservaEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion FROM estadoreserva WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilTexto.esNula(filtro.getNombre())) {
				sql.append(" AND nombre = ?");
				parametros.add(filtro.getNombre());
			}
			if (!UtilObjeto.esNulo(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
		}

		final List<EstadoReservaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirEstadoReservaEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los estados de reserva por filtro.");
		}
		return resultados;
	}

	private EstadoReservaEntidad construirEstadoReservaEntidad(final ResultSet rs) throws SQLException {
		return new EstadoReservaEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.nombre(rs.getString("nombre"))
				.descripcion(rs.getString("descripcion"))
				.build();
	}

}
