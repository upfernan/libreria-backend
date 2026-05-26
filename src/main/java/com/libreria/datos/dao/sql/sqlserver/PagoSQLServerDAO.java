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

import com.libreria.datos.dao.PagoDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.entidad.MultaEntidad;
import com.libreria.entidad.PagoEntidad;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class PagoSQLServerDAO extends SQLDAO implements PagoDAO {

	private static final Logger logger = LoggerFactory.getLogger(PagoSQLServerDAO.class);

	private static final String SELECT_BASE =
			"SELECT p.id, p.fechaPago, p.multaId,"
			+ " m.montoTotal, m.fechaGeneracion AS multaFechaGeneracion, m.pagada, m.diasRetraso,"
			+ " m.tarifaMultaId, tm.valorDiario AS tarifaValorDiario,"
			+ " m.devolucionId,"
			+ " m.usuarioAfectadoId, u.primerNombre AS usuarioPrimerNombre, u.primerApellido AS usuarioPrimerApellido"
			+ " FROM pago p"
			+ " LEFT JOIN multa m ON p.multaId = m.id"
			+ " LEFT JOIN tarifamulta tm ON m.tarifaMultaId = tm.id"
			+ " LEFT JOIN usuario u ON m.usuarioAfectadoId = u.id";

	public PagoSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final PagoEntidad entidad) {
		logger.debug("Entre al metodo crear de PagoSQLServerDAO...");
		final String sql = "INSERT INTO pago (id, fechaPago, multaId) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setDate(2, java.sql.Date.valueOf(entidad.getFechaPago()));
			ps.setString(3, entidad.getMulta().getId().toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de PagoSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el pago.",
					"Se presento una SQLException al ejecutar INSERT en la tabla pago desde PagoSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final PagoEntidad entidad) {
		logger.debug("Entre al metodo actualizar de PagoSQLServerDAO...");
		final String sql = "UPDATE pago SET fechaPago = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setDate(1, java.sql.Date.valueOf(entidad.getFechaPago()));
			ps.setString(2, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de PagoSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el pago.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla pago desde PagoSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de PagoSQLServerDAO...");
		final String sql = "DELETE FROM pago WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de PagoSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el pago.",
					"Se presento una SQLException al ejecutar DELETE en la tabla pago desde PagoSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<PagoEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de PagoSQLServerDAO...");
		final List<PagoEntidad> resultado = consultarPorFiltro(new PagoEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de PagoSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public PagoEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de PagoSQLServerDAO...");
		final String sql = SELECT_BASE + " WHERE p.id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final PagoEntidad resultado = construirPagoEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de PagoSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de PagoSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el pago por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla pago desde PagoSQLServerDAO.consultarPorId.");
		}
		return new PagoEntidad.Builder().build();
	}

	@Override
	public List<PagoEntidad> consultarPorFiltro(final PagoEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de PagoSQLServerDAO...");
		final StringBuilder sql = new StringBuilder(SELECT_BASE + " WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro) && !UtilObjeto.esNulo(filtro.getMulta()) && UtilUUID.tieneValor(filtro.getMulta().getId())) {
			sql.append(" AND p.multaId = ?");
			parametros.add(filtro.getMulta().getId().toString());
		}

		final List<PagoEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirPagoEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de PagoSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los pagos por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla pago desde PagoSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private PagoEntidad construirPagoEntidad(final ResultSet rs) throws SQLException {
		final String multaId          = rs.getString("multaId");
		final String tarifaMultaId    = rs.getString("tarifaMultaId");
		final String devolucionId     = rs.getString("devolucionId");
		final String usuarioAfectadoId = rs.getString("usuarioAfectadoId");
		final java.sql.Date multaFecha = rs.getDate("multaFechaGeneracion");

		final MultaEntidad.Builder multaBuilder = new MultaEntidad.Builder();
		if (multaId != null) {
			multaBuilder.id(UUID.fromString(multaId))
					.montoTotal(rs.getDouble("montoTotal"))
					.fechaGeneracion(multaFecha != null ? multaFecha.toLocalDate() : com.libreria.transversal.utilitario.UtilFecha.FECHA_DEFECTO)
					.pagada(rs.getBoolean("pagada"))
					.diasRetraso(rs.getInt("diasRetraso"))
					.tarifaMulta(tarifaMultaId != null
							? new TarifaMultaEntidad.Builder()
									.id(UUID.fromString(tarifaMultaId))
									.valorDiario(rs.getDouble("tarifaValorDiario"))
									.build()
							: new TarifaMultaEntidad.Builder().build())
					.devolucion(devolucionId != null
							? new DevolucionEntidad.Builder()
									.id(UUID.fromString(devolucionId))
									.build()
							: new DevolucionEntidad.Builder().build())
					.usuarioAfectado(usuarioAfectadoId != null
							? new UsuarioEntidad.Builder()
									.id(UUID.fromString(usuarioAfectadoId))
									.primerNombre(rs.getString("usuarioPrimerNombre"))
									.primerApellido(rs.getString("usuarioPrimerApellido"))
									.build()
							: new UsuarioEntidad.Builder().build());
		}

		return new PagoEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.fechaPago(rs.getDate("fechaPago").toLocalDate())
				.multa(multaBuilder.build())
				.build();
	}

}
