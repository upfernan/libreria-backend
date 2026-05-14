package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.TipoLibroDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class TipoLibroSQLServerDAO extends SQLDAO implements TipoLibroDAO {

	public TipoLibroSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final TipoLibroEntidad entidad) {
		final String sql = "INSERT INTO tipolibro (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar el tipo de libro.");
		}
	}

	@Override
	public void actualizar(final UUID id, final TipoLibroEntidad entidad) {
		final String sql = "UPDATE tipolibro SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar el tipo de libro.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM tipolibro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar el tipo de libro.");
		}
	}

	@Override
	public List<TipoLibroEntidad> consultarTodos() {
		final String sql = "SELECT id, nombre, descripcion FROM tipolibro";
		final List<TipoLibroEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirTipoLibroEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los tipos de libro.");
		}
		return resultados;
	}

	@Override
	public TipoLibroEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, nombre, descripcion FROM tipolibro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirTipoLibroEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar el tipo de libro por identificador.");
		}
		return null;
	}

	@Override
	public List<TipoLibroEntidad> consultarPorFiltro(final TipoLibroEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion FROM tipolibro WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (!UtilTexto.esNula(filtro.getNombre())) {
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
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los tipos de libro por filtro.");
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
