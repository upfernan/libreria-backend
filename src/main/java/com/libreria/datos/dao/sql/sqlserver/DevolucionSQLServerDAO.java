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

import com.libreria.datos.dao.DevolucionDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class DevolucionSQLServerDAO extends SQLDAO implements DevolucionDAO {

	private static final Logger logger = LoggerFactory.getLogger(DevolucionSQLServerDAO.class);

	private static final String SELECT_BASE =
			"SELECT d.id, d.fechaDevolucion, d.prestamoId,"
			+ " u.primerNombre AS usuarioPrimerNombre, u.primerApellido AS usuarioPrimerApellido,"
			+ " l.titulo AS libroTitulo"
			+ " FROM devolucion d"
			+ " LEFT JOIN prestamo p ON d.prestamoId = p.id"
			+ " LEFT JOIN usuario u ON p.usuarioId = u.id"
			+ " LEFT JOIN ejemplar e ON p.ejemplarId = e.id"
			+ " LEFT JOIN libro l ON e.libroId = l.id";

	public DevolucionSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final DevolucionEntidad entidad) {
		logger.debug("Entre al metodo crear de DevolucionSQLServerDAO...");
		final String sql = "INSERT INTO devolucion (id, fechaDevolucion, prestamoId) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setDate(2, java.sql.Date.valueOf(entidad.getFechaDevolucion()));
			ps.setString(3, entidad.getPrestamo().getId().toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de DevolucionSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar la devolución.",
					"Se presento una SQLException al ejecutar INSERT en la tabla devolucion desde DevolucionSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final DevolucionEntidad entidad) {
		logger.debug("Entre al metodo actualizar de DevolucionSQLServerDAO...");
		final String sql = "UPDATE devolucion SET fechaDevolucion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setDate(1, java.sql.Date.valueOf(entidad.getFechaDevolucion()));
			ps.setString(2, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de DevolucionSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar la devolución.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla devolucion desde DevolucionSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de DevolucionSQLServerDAO...");
		final String sql = "DELETE FROM devolucion WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de DevolucionSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar la devolución.",
					"Se presento una SQLException al ejecutar DELETE en la tabla devolucion desde DevolucionSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<DevolucionEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de DevolucionSQLServerDAO...");
		final List<DevolucionEntidad> resultado = consultarPorFiltro(new DevolucionEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de DevolucionSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public DevolucionEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de DevolucionSQLServerDAO...");
		final String sql = SELECT_BASE + " WHERE d.id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final DevolucionEntidad resultado = construirDevolucionEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de DevolucionSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de DevolucionSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar la devolución por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla devolucion desde DevolucionSQLServerDAO.consultarPorId.");
		}
		return new DevolucionEntidad.Builder().build();
	}

	@Override
	public List<DevolucionEntidad> consultarPorFiltro(final DevolucionEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de DevolucionSQLServerDAO...");
		final StringBuilder sql = new StringBuilder(SELECT_BASE + " WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro) && !UtilObjeto.esNulo(filtro.getPrestamo()) && UtilUUID.tieneValor(filtro.getPrestamo().getId())) {
			sql.append(" AND d.prestamoId = ?");
			parametros.add(filtro.getPrestamo().getId().toString());
		}

		final List<DevolucionEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirDevolucionEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de DevolucionSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las devoluciones por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla devolucion desde DevolucionSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private DevolucionEntidad construirDevolucionEntidad(final ResultSet rs) throws SQLException {
		return new DevolucionEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.fechaDevolucion(rs.getDate("fechaDevolucion").toLocalDate())
				.prestamo(new PrestamoEntidad.Builder()
						.id(UUID.fromString(rs.getString("prestamoId")))
						.usuario(new UsuarioEntidad.Builder()
								.primerNombre(rs.getString("usuarioPrimerNombre"))
								.primerApellido(rs.getString("usuarioPrimerApellido"))
								.build())
						.ejemplar(new EjemplarEntidad.Builder()
								.libro(new LibroEntidad.Builder()
										.titulo(rs.getString("libroTitulo"))
										.build())
								.build())
						.build())
				.build();
	}

}
