package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.libreria.datos.dao.LibroDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class LibroSQLServerDAO extends SQLDAO implements LibroDAO {

	private static final Logger logger = LoggerFactory.getLogger(LibroSQLServerDAO.class);

	private static final String SELECT_BASE =
			"SELECT l.id, l.titulo, l.disponibles,"
			+ " l.tipoLibroId,  tl.nombre AS tipoLibroNombre,"
			+ " l.categoriaId,   c.nombre  AS categoriaNombre,"
			+ " l.editorialId,   e.nombre  AS editorialNombre"
			+ " FROM libro l"
			+ " LEFT JOIN tipolibro tl ON l.tipoLibroId = tl.id"
			+ " LEFT JOIN categoria  c ON l.categoriaId  = c.id"
			+ " LEFT JOIN editorial  e ON l.editorialId  = e.id";

	public LibroSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final LibroEntidad entidad) {
		logger.debug("Entre al metodo crear de LibroSQLServerDAO...");
		final String sql = "INSERT INTO libro (id, titulo, tipoLibroId, categoriaId, editorialId, disponibles) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getTitulo());
			ps.setString(3, entidad.getTipoLibro().getId().toString());
			ps.setString(4, entidad.getCategoria().getId().toString());
			ps.setString(5, entidad.getEditorial().getId().toString());
			ps.setInt(6, entidad.getDisponibles());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de LibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el libro.",
					"Se presento una SQLException al ejecutar INSERT en la tabla libro desde LibroSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final LibroEntidad entidad) {
		logger.debug("Entre al metodo actualizar de LibroSQLServerDAO...");
		final String sql = "UPDATE libro SET titulo = ?, tipoLibroId = ?, categoriaId = ?, editorialId = ?, disponibles = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getTitulo());
			ps.setString(2, entidad.getTipoLibro().getId().toString());
			ps.setString(3, entidad.getCategoria().getId().toString());
			ps.setString(4, entidad.getEditorial().getId().toString());
			ps.setInt(5, entidad.getDisponibles());
			ps.setString(6, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de LibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el libro.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla libro desde LibroSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de LibroSQLServerDAO...");
		final String sql = "DELETE FROM libro WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de LibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el libro.",
					"Se presento una SQLException al ejecutar DELETE en la tabla libro desde LibroSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<LibroEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de LibroSQLServerDAO...");
		final List<LibroEntidad> resultado = consultarPorFiltro(new LibroEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de LibroSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public LibroEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de LibroSQLServerDAO...");
		final String sql = SELECT_BASE + " WHERE l.id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final LibroEntidad resultado = construirLibroEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de LibroSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de LibroSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el libro por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla libro desde LibroSQLServerDAO.consultarPorId.");
		}
		return null;
	}

	@Override
	public List<LibroEntidad> consultarPorFiltro(final LibroEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de LibroSQLServerDAO...");
		final StringBuilder sql = new StringBuilder(SELECT_BASE + " WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		aplicarFiltros(filtro, sql, parametros);

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
			logger.debug("Sali del metodo consultarPorFiltro de LibroSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los libros por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla libro desde LibroSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private void aplicarFiltros(final LibroEntidad filtro, final StringBuilder sql, final List<Object> parametros) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}
		if (UtilUUID.tieneValor(filtro.getId())) {
			sql.append(" AND l.id = ?");
			parametros.add(filtro.getId().toString());
		}
		if (UtilTexto.tieneContenido(filtro.getTitulo())) {
			sql.append(" AND l.titulo = ?");
			parametros.add(filtro.getTitulo());
		}
		if (!UtilObjeto.esNulo(filtro.getTipoLibro()) && UtilUUID.tieneValor(filtro.getTipoLibro().getId())) {
			sql.append(" AND l.tipoLibroId = ?");
			parametros.add(filtro.getTipoLibro().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getCategoria()) && UtilUUID.tieneValor(filtro.getCategoria().getId())) {
			sql.append(" AND l.categoriaId = ?");
			parametros.add(filtro.getCategoria().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getEditorial()) && UtilUUID.tieneValor(filtro.getEditorial().getId())) {
			sql.append(" AND l.editorialId = ?");
			parametros.add(filtro.getEditorial().getId().toString());
		}
	}

	private LibroEntidad construirLibroEntidad(final ResultSet rs) throws SQLException {
		return new LibroEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.titulo(rs.getString("titulo"))
				.disponibles(rs.getInt("disponibles"))
				.tipoLibro(new TipoLibroEntidad.Builder()
						.id(UUID.fromString(rs.getString("tipoLibroId")))
						.nombre(rs.getString("tipoLibroNombre"))
						.build())
				.categoria(new CategoriaEntidad.Builder()
						.id(UUID.fromString(rs.getString("categoriaId")))
						.nombre(rs.getString("categoriaNombre"))
						.build())
				.editorial(new EditorialEntidad.Builder()
						.id(UUID.fromString(rs.getString("editorialId")))
						.nombre(rs.getString("editorialNombre"))
						.build())
				.build();
	}

}
