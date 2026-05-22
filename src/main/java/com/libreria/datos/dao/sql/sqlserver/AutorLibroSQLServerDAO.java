package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.AutorLibroDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.AutorEntidad;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AutorLibroSQLServerDAO extends SQLDAO implements AutorLibroDAO {

	public AutorLibroSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final AutorLibroEntidad entidad) {
		final String sql = "INSERT INTO autorlibro (id, autorId, libroId) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getAutor().getId().toString());
			ps.setString(3, entidad.getLibro().getId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el autor-libro.");
		}
	}

	@Override
	public void actualizar(final UUID id, final AutorLibroEntidad entidad) {
		final String sql = "UPDATE autorlibro SET autorId = ?, libroId = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getAutor().getId().toString());
			ps.setString(2, entidad.getLibro().getId().toString());
			ps.setString(3, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el autor-libro.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM autorlibro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el autor-libro.");
		}
	}

	@Override
	public List<AutorLibroEntidad> consultarTodos() {
		final String sql = "SELECT id, autorId, libroId FROM autorlibro";
		final List<AutorLibroEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirAutorLibroEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los autores-libro.");
		}
		return resultados;
	}

	@Override
	public AutorLibroEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, autorId, libroId FROM autorlibro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirAutorLibroEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el autor-libro por identificador.");
		}
		return null;
	}

	@Override
	public List<AutorLibroEntidad> consultarPorFiltro(final AutorLibroEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, autorId, libroId FROM autorlibro WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getAutor()) && UtilUUID.tieneValor(filtro.getAutor().getId())) {
				sql.append(" AND autorId = ?");
				parametros.add(filtro.getAutor().getId().toString());
			}
			if (!UtilObjeto.esNulo(filtro.getLibro()) && UtilUUID.tieneValor(filtro.getLibro().getId())) {
				sql.append(" AND libroId = ?");
				parametros.add(filtro.getLibro().getId().toString());
			}
		}

		final List<AutorLibroEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirAutorLibroEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los autores-libro por filtro.");
		}
		return resultados;
	}

	private AutorLibroEntidad construirAutorLibroEntidad(final ResultSet rs) throws SQLException {
		return new AutorLibroEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.autor(new AutorEntidad.Builder()
						.id(UUID.fromString(rs.getString("autorId")))
						.build())
				.libro(new LibroEntidad.Builder()
						.id(UUID.fromString(rs.getString("libroId")))
						.build())
				.build();
	}

}
