package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.ReservaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ReservaSQLServerDAO extends SQLDAO implements ReservaDAO {

	private static final String SELECT_BASE =
			"SELECT r.id, r.fechaReserva, r.fechaExpiracion,"
			+ " r.estadoReservaId, er.nombre AS estadoReservaNombre,"
			+ " r.usuarioId, u.primerNombre AS usuarioPrimerNombre, u.primerApellido AS usuarioPrimerApellido,"
			+ " r.libroId, l.titulo AS libroTitulo"
			+ " FROM reserva r"
			+ " LEFT JOIN estadoreserva er ON r.estadoReservaId = er.id"
			+ " LEFT JOIN usuario u ON r.usuarioId = u.id"
			+ " LEFT JOIN libro l ON r.libroId = l.id";

	public ReservaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final ReservaEntidad entidad) {
		final String sql = "INSERT INTO reserva (id, fechaReserva, fechaExpiracion, estadoReservaId, usuarioId, libroId) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setDate(2, java.sql.Date.valueOf(entidad.getFechaReserva()));
			ps.setDate(3, java.sql.Date.valueOf(entidad.getFechaExpiracion()));
			ps.setString(4, entidad.getEstadoReserva().getId().toString());
			ps.setString(5, entidad.getUsuario().getId().toString());
			ps.setString(6, entidad.getLibro().getId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar la reserva.");
		}
	}

	@Override
	public void actualizar(final UUID id, final ReservaEntidad entidad) {
		final String sql = "UPDATE reserva SET estadoReservaId = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getEstadoReserva().getId().toString());
			ps.setString(2, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar la reserva.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM reserva WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar la reserva.");
		}
	}

	@Override
	public List<ReservaEntidad> consultarTodos() {
		final List<ReservaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(SELECT_BASE);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirReservaEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las reservas.");
		}
		return resultados;
	}

	@Override
	public ReservaEntidad consultarPorId(final UUID id) {
		final String sql = SELECT_BASE + " WHERE r.id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirReservaEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar la reserva por identificador.");
		}
		return null;
	}

	@Override
	public List<ReservaEntidad> consultarPorFiltro(final ReservaEntidad filtro) {
		final StringBuilder sql = new StringBuilder(SELECT_BASE + " WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		aplicarFiltros(filtro, sql, parametros);

		sql.append(" ORDER BY r.fechaReserva ASC");

		final List<ReservaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirReservaEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las reservas por filtro.");
		}
		return resultados;
	}

	private void aplicarFiltros(final ReservaEntidad filtro, final StringBuilder sql, final List<Object> parametros) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}
		if (!UtilObjeto.esNulo(filtro.getLibro()) && UtilUUID.tieneValor(filtro.getLibro().getId())) {
			sql.append(" AND r.libroId = ?");
			parametros.add(filtro.getLibro().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getUsuario()) && UtilUUID.tieneValor(filtro.getUsuario().getId())) {
			sql.append(" AND r.usuarioId = ?");
			parametros.add(filtro.getUsuario().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getEstadoReserva()) && UtilUUID.tieneValor(filtro.getEstadoReserva().getId())) {
			sql.append(" AND r.estadoReservaId = ?");
			parametros.add(filtro.getEstadoReserva().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getEstadoReserva()) && UtilTexto.tieneContenido(filtro.getEstadoReserva().getNombre())) {
			sql.append(" AND er.nombre = ?");
			parametros.add(filtro.getEstadoReserva().getNombre());
		}
	}

	private ReservaEntidad construirReservaEntidad(final ResultSet rs) throws SQLException {
		return new ReservaEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.fechaReserva(rs.getDate("fechaReserva").toLocalDate())
				.fechaExpiracion(rs.getDate("fechaExpiracion").toLocalDate())
				.estadoReserva(new EstadoReservaEntidad.Builder()
						.id(UUID.fromString(rs.getString("estadoReservaId")))
						.nombre(rs.getString("estadoReservaNombre"))
						.build())
				.usuario(new UsuarioEntidad.Builder()
						.id(UUID.fromString(rs.getString("usuarioId")))
						.primerNombre(rs.getString("usuarioPrimerNombre"))
						.primerApellido(rs.getString("usuarioPrimerApellido"))
						.build())
				.libro(new LibroEntidad.Builder()
						.id(UUID.fromString(rs.getString("libroId")))
						.titulo(rs.getString("libroTitulo"))
						.build())
				.build();
	}

}
