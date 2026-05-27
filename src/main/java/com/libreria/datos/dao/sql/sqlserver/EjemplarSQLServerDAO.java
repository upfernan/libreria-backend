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

import com.libreria.datos.dao.EjemplarDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class EjemplarSQLServerDAO extends SQLDAO implements EjemplarDAO {

	private static final Logger logger = LoggerFactory.getLogger(EjemplarSQLServerDAO.class);

	private static final String SELECT_BASE =
			"SELECT e.id, e.libroId, e.signaturaId,"
			+ " l.titulo AS libroTitulo,"
			+ " s.pasillo AS sigPasillo, s.estante AS sigEstante, s.posicion AS sigPosicion"
			+ " FROM ejemplar e"
			+ " LEFT JOIN libro l ON e.libroId = l.id"
			+ " LEFT JOIN signatura s ON e.signaturaId = s.id";

	public EjemplarSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final EjemplarEntidad entidad) {
		logger.debug("Entre al metodo crear de EjemplarSQLServerDAO...");
		final String sql = "INSERT INTO ejemplar (id, libroId, signaturaId) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getLibro().getId().toString());
			final String signaturaId = !UtilObjeto.esNulo(entidad.getSignatura()) && !UtilObjeto.esNulo(entidad.getSignatura().getId())
					? entidad.getSignatura().getId().toString()
					: null;
			ps.setString(3, signaturaId);
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de EjemplarSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el ejemplar.",
					"Se presento una SQLException al ejecutar INSERT en la tabla ejemplar desde EjemplarSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final EjemplarEntidad entidad) {
		logger.debug("Entre al metodo actualizar de EjemplarSQLServerDAO...");
		final String sql = "UPDATE ejemplar SET libroId = ?, signaturaId = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getLibro().getId().toString());
			final String signaturaId = !UtilObjeto.esNulo(entidad.getSignatura()) && !UtilObjeto.esNulo(entidad.getSignatura().getId())
					? entidad.getSignatura().getId().toString()
					: null;
			ps.setString(2, signaturaId);
			ps.setString(3, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de EjemplarSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el ejemplar.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla ejemplar desde EjemplarSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de EjemplarSQLServerDAO...");
		final String sql = "DELETE FROM ejemplar WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de EjemplarSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el ejemplar.",
					"Se presento una SQLException al ejecutar DELETE en la tabla ejemplar desde EjemplarSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<EjemplarEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de EjemplarSQLServerDAO...");
		final List<EjemplarEntidad> resultado = consultarPorFiltro(new EjemplarEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de EjemplarSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public EjemplarEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de EjemplarSQLServerDAO...");
		final String sql = SELECT_BASE + " WHERE e.id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final EjemplarEntidad resultado = construirEjemplarEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de EjemplarSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de EjemplarSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el ejemplar por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla ejemplar desde EjemplarSQLServerDAO.consultarPorId.");
		}
		return new EjemplarEntidad.Builder().build();
	}

	@Override
	public List<EjemplarEntidad> consultarPorFiltro(final EjemplarEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de EjemplarSQLServerDAO...");
		final StringBuilder sql = new StringBuilder(SELECT_BASE + " WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (UtilUUID.tieneValor(filtro.getId())) {
				sql.append(" AND e.id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (!UtilObjeto.esNulo(filtro.getLibro()) && UtilUUID.tieneValor(filtro.getLibro().getId())) {
				sql.append(" AND e.libroId = ?");
				parametros.add(filtro.getLibro().getId().toString());
			}
			if (!UtilObjeto.esNulo(filtro.getSignatura()) && UtilUUID.tieneValor(filtro.getSignatura().getId())) {
				sql.append(" AND e.signaturaId = ?");
				parametros.add(filtro.getSignatura().getId().toString());
			}
		}

		final List<EjemplarEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirEjemplarEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de EjemplarSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los ejemplares por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla ejemplar desde EjemplarSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private EjemplarEntidad construirEjemplarEntidad(final ResultSet rs) throws SQLException {
		final String signaturaId = rs.getString("signaturaId");
		final String sigPasilloStr = rs.getString("sigPasillo");
		final char sigPasillo = sigPasilloStr != null && !sigPasilloStr.isEmpty() ? sigPasilloStr.charAt(0) : '\0';
		final SignaturaEntidad signatura = UtilObjeto.esNulo(signaturaId)
				? new SignaturaEntidad.Builder().build()
				: new SignaturaEntidad.Builder()
						.id(UUID.fromString(signaturaId))
						.pasillo(sigPasillo)
						.estante(rs.getInt("sigEstante"))
						.posicion(rs.getInt("sigPosicion"))
						.build();
		return new EjemplarEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.libro(new LibroEntidad.Builder()
						.id(UUID.fromString(rs.getString("libroId")))
						.titulo(rs.getString("libroTitulo"))
						.build())
				.signatura(signatura)
				.build();
	}

}
