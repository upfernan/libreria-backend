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

import com.libreria.datos.dao.EditorialDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class EditorialSQLServerDAO extends SQLDAO implements EditorialDAO {

	private static final Logger logger = LoggerFactory.getLogger(EditorialSQLServerDAO.class);

	public EditorialSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final EditorialEntidad entidad) {
		logger.debug("Entre al metodo crear de EditorialSQLServerDAO...");
		final String sql = "INSERT INTO editorial (id, nit, nombre) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNit());
			ps.setString(3, entidad.getNombre());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de EditorialSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar la editorial.",
					"Se presento una SQLException al ejecutar INSERT en la tabla editorial desde EditorialSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final EditorialEntidad entidad) {
		logger.debug("Entre al metodo actualizar de EditorialSQLServerDAO...");
		final String sql = "UPDATE editorial SET nit = ?, nombre = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNit());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de EditorialSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar la editorial.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla editorial desde EditorialSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de EditorialSQLServerDAO...");
		final String sql = "DELETE FROM editorial WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de EditorialSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar la editorial.",
					"Se presento una SQLException al ejecutar DELETE en la tabla editorial desde EditorialSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<EditorialEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de EditorialSQLServerDAO...");
		final List<EditorialEntidad> resultado = consultarPorFiltro(new EditorialEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de EditorialSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public EditorialEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de EditorialSQLServerDAO...");
		final String sql = "SELECT id, nit, nombre FROM editorial WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final EditorialEntidad resultado = construirEditorialEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de EditorialSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de EditorialSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar la editorial por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla editorial desde EditorialSQLServerDAO.consultarPorId.");
		}
		return new EditorialEntidad.Builder().build();
	}

	@Override
	public List<EditorialEntidad> consultarPorFiltro(final EditorialEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de EditorialSQLServerDAO...");
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
			logger.debug("Sali del metodo consultarPorFiltro de EditorialSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las editoriales por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla editorial desde EditorialSQLServerDAO.consultarPorFiltro.");
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
