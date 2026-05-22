package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.TipoIdentificacionDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class TipoIdentificacionSQLServerDAO extends SQLDAO implements TipoIdentificacionDAO {

	public TipoIdentificacionSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final TipoIdentificacionEntidad entidad) {
		final String sql = "INSERT INTO tipoidentificacion (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el tipo de identificación.");
		}
	}

	@Override
	public void actualizar(final UUID id, final TipoIdentificacionEntidad entidad) {
		final String sql = "UPDATE tipoidentificacion SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el tipo de identificación.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM tipoidentificacion WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el tipo de identificación.");
		}
	}

	@Override
	public List<TipoIdentificacionEntidad> consultarTodos() {
		final String sql = "SELECT id, nombre, descripcion FROM tipoidentificacion";
		final List<TipoIdentificacionEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirTipoIdentificacionEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los tipos de identificación.");
		}
		return resultados;
	}

	@Override
	public TipoIdentificacionEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, nombre, descripcion FROM tipoidentificacion WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirTipoIdentificacionEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el tipo de identificación por identificador.");
		}
		return null;
	}

	@Override
	public List<TipoIdentificacionEntidad> consultarPorFiltro(final TipoIdentificacionEntidad filtro) {
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
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los tipos de identificación por filtro.");
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
