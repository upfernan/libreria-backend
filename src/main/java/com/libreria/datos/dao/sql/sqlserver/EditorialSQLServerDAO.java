package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.EditorialDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class EditorialSQLServerDAO extends SQLDAO implements EditorialDAO {

	public EditorialSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final EditorialEntidad entidad) {
		final String sql = "INSERT INTO editorial (id, nit, nombre) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNit());
			ps.setString(3, entidad.getNombre());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar la editorial.");
		}
	}

	@Override
	public void actualizar(final UUID id, final EditorialEntidad entidad) {
		final String sql = "UPDATE editorial SET nit = ?, nombre = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNit());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar la editorial.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM editorial WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar la editorial.");
		}
	}

	@Override
	public List<EditorialEntidad> consultarTodos() {
		final String sql = "SELECT id, nit, nombre FROM editorial";
		final List<EditorialEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirEditorialEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las editoriales.");
		}
		return resultados;
	}

	@Override
	public EditorialEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, nit, nombre FROM editorial WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirEditorialEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar la editorial por identificador.");
		}
		return null;
	}

	@Override
	public List<EditorialEntidad> consultarPorFiltro(final EditorialEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, nit, nombre FROM editorial WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (UtilUUID.tieneValor(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (UtilTexto.tieneContenido(filtro.getNit())) {
				sql.append(" AND nit = ?");
				parametros.add(filtro.getNit());
			}
			if (UtilTexto.tieneContenido(filtro.getNombre())) {
				sql.append(" AND nombre = ?");
				parametros.add(filtro.getNombre());
			}
		}

		final List<EditorialEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirEditorialEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las editoriales por filtro.");
		}
		return resultados;
	}

	private EditorialEntidad construirEditorialEntidad(final ResultSet rs) throws SQLException {
		return new EditorialEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.nit(rs.getString("nit"))
				.nombre(rs.getString("nombre"))
				.build();
	}

}
