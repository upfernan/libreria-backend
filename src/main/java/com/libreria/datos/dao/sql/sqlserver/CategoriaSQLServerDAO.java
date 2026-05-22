package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.CategoriaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class CategoriaSQLServerDAO extends SQLDAO implements CategoriaDAO {

	public CategoriaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final CategoriaEntidad entidad) {
		final String sql = "INSERT INTO categoria (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar la categoría.");
		}
	}

	@Override
	public void actualizar(final UUID id, final CategoriaEntidad entidad) {
		final String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar la categoría.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM categoria WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar la categoría.");
		}
	}

	@Override
	public List<CategoriaEntidad> consultarTodos() {
		final String sql = "SELECT id, nombre, descripcion FROM categoria";
		final List<CategoriaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirCategoriaEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las categorías.");
		}
		return resultados;
	}

	@Override
	public CategoriaEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, nombre, descripcion FROM categoria WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirCategoriaEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar la categoría por identificador.");
		}
		return null;
	}

	@Override
	public List<CategoriaEntidad> consultarPorFiltro(final CategoriaEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion FROM categoria WHERE 1=1");
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

		final List<CategoriaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirCategoriaEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las categorías por filtro.");
		}
		return resultados;
	}

	private CategoriaEntidad construirCategoriaEntidad(final ResultSet rs) throws SQLException {
		return new CategoriaEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.nombre(rs.getString("nombre"))
				.descripcion(rs.getString("descripcion"))
				.build();
	}

}
