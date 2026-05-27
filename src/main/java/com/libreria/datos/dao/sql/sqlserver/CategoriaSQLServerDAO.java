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

import com.libreria.datos.dao.CategoriaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class CategoriaSQLServerDAO extends SQLDAO implements CategoriaDAO {

	private static final Logger logger = LoggerFactory.getLogger(CategoriaSQLServerDAO.class);

	public CategoriaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final CategoriaEntidad entidad) {
		logger.debug("Entre al metodo crear de CategoriaSQLServerDAO...");
		final String sql = "INSERT INTO categoria (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de CategoriaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar la categoría.",
					"Se presento una SQLException al ejecutar INSERT en la tabla categoria desde CategoriaSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final CategoriaEntidad entidad) {
		logger.debug("Entre al metodo actualizar de CategoriaSQLServerDAO...");
		final String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de CategoriaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar la categoría.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla categoria desde CategoriaSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de CategoriaSQLServerDAO...");
		final String sql = "DELETE FROM categoria WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de CategoriaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar la categoría.",
					"Se presento una SQLException al ejecutar DELETE en la tabla categoria desde CategoriaSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<CategoriaEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de CategoriaSQLServerDAO...");
		final List<CategoriaEntidad> resultado = consultarPorFiltro(new CategoriaEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de CategoriaSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public CategoriaEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de CategoriaSQLServerDAO...");
		final String sql = "SELECT id, nombre, descripcion FROM categoria WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final CategoriaEntidad resultado = construirCategoriaEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de CategoriaSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de CategoriaSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar la categoría por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla categoria desde CategoriaSQLServerDAO.consultarPorId.");
		}
		return new CategoriaEntidad.Builder().build();
	}

	@Override
	public List<CategoriaEntidad> consultarPorFiltro(final CategoriaEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de CategoriaSQLServerDAO...");
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
			logger.debug("Sali del metodo consultarPorFiltro de CategoriaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las categorías por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla categoria desde CategoriaSQLServerDAO.consultarPorFiltro.");
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
