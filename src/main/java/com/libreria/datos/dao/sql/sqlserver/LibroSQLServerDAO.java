package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.LibroDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class LibroSQLServerDAO extends SQLDAO implements LibroDAO {

	public LibroSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final LibroEntidad entidad) {
		final String sql = "INSERT INTO libro (id, titulo, tipoLibroId, categoriaId, editorialId, disponibles) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getTitulo());
			ps.setString(3, entidad.getTipoLibro().getId().toString());
			ps.setString(4, entidad.getCategoria().getId().toString());
			ps.setString(5, entidad.getEditorial().getId().toString());
			ps.setInt(6, entidad.getDisponibles());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar el libro.");
		}
	}

	@Override
	public void actualizar(final UUID id, final LibroEntidad entidad) {
		final String sql = "UPDATE libro SET titulo = ?, tipoLibroId = ?, categoriaId = ?, editorialId = ?, disponibles = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getTitulo());
			ps.setString(2, entidad.getTipoLibro().getId().toString());
			ps.setString(3, entidad.getCategoria().getId().toString());
			ps.setString(4, entidad.getEditorial().getId().toString());
			ps.setInt(5, entidad.getDisponibles());
			ps.setString(6, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar el libro.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM libro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar el libro.");
		}
	}

	@Override
	public List<LibroEntidad> consultarTodos() {
		final String sql = "SELECT id, titulo, tipoLibroId, categoriaId, editorialId, disponibles FROM libro";
		final List<LibroEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirLibroEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los libros.");
		}
		return resultados;
	}

	@Override
	public LibroEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, titulo, tipoLibroId, categoriaId, editorialId, disponibles FROM libro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirLibroEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar el libro por identificador.");
		}
		return null;
	}

	@Override
	public List<LibroEntidad> consultarPorFiltro(final LibroEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, titulo, tipoLibroId, categoriaId, editorialId, disponibles FROM libro WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (!UtilTexto.esNula(filtro.getTitulo())) {
				sql.append(" AND titulo = ?");
				parametros.add(filtro.getTitulo());
			}
		}

		final List<LibroEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirLibroEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los libros por filtro.");
		}
		return resultados;
	}

	private LibroEntidad construirLibroEntidad(final ResultSet rs) throws SQLException {
		return new LibroEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.titulo(rs.getString("titulo"))
				.tipoLibro(new TipoLibroEntidad.Builder()
						.id(UUID.fromString(rs.getString("tipoLibroId")))
						.build())
				.categoria(new CategoriaEntidad.Builder()
						.id(UUID.fromString(rs.getString("categoriaId")))
						.build())
				.editorial(new EditorialEntidad.Builder()
						.id(UUID.fromString(rs.getString("editorialId")))
						.build())
				.disponibles(rs.getInt("disponibles"))
				.build();
	}

}
