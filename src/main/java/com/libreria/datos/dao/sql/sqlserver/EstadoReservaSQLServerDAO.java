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

import com.libreria.datos.dao.EstadoReservaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class EstadoReservaSQLServerDAO extends SQLDAO implements EstadoReservaDAO {

	private static final Logger logger = LoggerFactory.getLogger(EstadoReservaSQLServerDAO.class);

	public EstadoReservaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final EstadoReservaEntidad entidad) {
		logger.debug("Entre al metodo crear de EstadoReservaSQLServerDAO...");
		final String sql = "INSERT INTO estadoreserva (id, nombre, descripcion) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getNombre());
			ps.setString(3, entidad.getDescripcion());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de EstadoReservaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el estado de reserva.",
					"Se presento una SQLException al ejecutar INSERT en la tabla estadoreserva desde EstadoReservaSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final EstadoReservaEntidad entidad) {
		logger.debug("Entre al metodo actualizar de EstadoReservaSQLServerDAO...");
		final String sql = "UPDATE estadoreserva SET nombre = ?, descripcion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getNombre());
			ps.setString(2, entidad.getDescripcion());
			ps.setString(3, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de EstadoReservaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el estado de reserva.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla estadoreserva desde EstadoReservaSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de EstadoReservaSQLServerDAO...");
		final String sql = "DELETE FROM estadoreserva WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de EstadoReservaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el estado de reserva.",
					"Se presento una SQLException al ejecutar DELETE en la tabla estadoreserva desde EstadoReservaSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<EstadoReservaEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de EstadoReservaSQLServerDAO...");
		final List<EstadoReservaEntidad> resultado = consultarPorFiltro(new EstadoReservaEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de EstadoReservaSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public EstadoReservaEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de EstadoReservaSQLServerDAO...");
		final String sql = "SELECT id, nombre, descripcion FROM estadoreserva WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final EstadoReservaEntidad resultado = construirEstadoReservaEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de EstadoReservaSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de EstadoReservaSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el estado de reserva por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla estadoreserva desde EstadoReservaSQLServerDAO.consultarPorId.");
		}
		return new EstadoReservaEntidad.Builder().build();
	}

	@Override
	public List<EstadoReservaEntidad> consultarPorFiltro(final EstadoReservaEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de EstadoReservaSQLServerDAO...");
		final StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion FROM estadoreserva WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (UtilTexto.tieneContenido(filtro.getNombre())) {
				sql.append(" AND nombre = ?");
				parametros.add(filtro.getNombre());
			}
			if (UtilUUID.tieneValor(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
		}

		final List<EstadoReservaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirEstadoReservaEntidad(rs));
				}
			}
			logger.debug("Sali del metodo consultarPorFiltro de EstadoReservaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los estados de reserva por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla estadoreserva desde EstadoReservaSQLServerDAO.consultarPorFiltro.");
		}
		return resultados;
	}

	private EstadoReservaEntidad construirEstadoReservaEntidad(final ResultSet rs) throws SQLException {
		return new EstadoReservaEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.nombre(rs.getString("nombre"))
				.descripcion(rs.getString("descripcion"))
				.build();
	}

}
