package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.PrestamoDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class PrestamoSQLServerDAO extends SQLDAO implements PrestamoDAO {

	private static final String SELECT_BASE =
			"SELECT p.id, p.fechaPrestamo, p.fechaDevolucionEsperada,"
			+ " p.estadoPrestamoId, ep.nombre AS estadoPrestamoNombre,"
			+ " p.reservaId, p.usuarioId,"
			+ " p.ejemplarId, e.libroId,"
			+ " l.titulo, l.disponibles, l.tipoLibroId, l.categoriaId, l.editorialId,"
			+ " tl.nombre AS tipoLibroNombre"
			+ " FROM prestamo p"
			+ " LEFT JOIN estadoprestamo ep ON p.estadoPrestamoId = ep.id"
			+ " LEFT JOIN ejemplar e ON p.ejemplarId = e.id"
			+ " LEFT JOIN libro l ON e.libroId = l.id"
			+ " LEFT JOIN tipolibro tl ON l.tipoLibroId = tl.id";

	public PrestamoSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final PrestamoEntidad entidad) {
		final String sql = "INSERT INTO prestamo (id, fechaPrestamo, fechaDevolucionEsperada, estadoPrestamoId, reservaId, usuarioId, ejemplarId) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setDate(2, java.sql.Date.valueOf(entidad.getFechaPrestamo()));
			ps.setDate(3, java.sql.Date.valueOf(entidad.getFechaDevolucionEsperada()));
			ps.setString(4, entidad.getEstadoPrestamo().getId().toString());
			ps.setString(5, entidad.getReserva().getId().toString());
			ps.setString(6, entidad.getUsuario().getId().toString());
			ps.setString(7, entidad.getEjemplar().getId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar el préstamo.");
		}
	}

	@Override
	public void actualizar(final UUID id, final PrestamoEntidad entidad) {
		final String sql = "UPDATE prestamo SET estadoPrestamoId = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getEstadoPrestamo().getId().toString());
			ps.setString(2, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar el préstamo.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM prestamo WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar el préstamo.");
		}
	}

	@Override
	public List<PrestamoEntidad> consultarTodos() {
		final List<PrestamoEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(SELECT_BASE);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirPrestamoEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los préstamos.");
		}
		return resultados;
	}

	@Override
	public PrestamoEntidad consultarPorId(final UUID id) {
		final String sql = SELECT_BASE + " WHERE p.id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirPrestamoEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar el préstamo por identificador.");
		}
		return null;
	}

	@Override
	public List<PrestamoEntidad> consultarPorFiltro(final PrestamoEntidad filtro) {
		final StringBuilder sql = new StringBuilder(SELECT_BASE + " WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();
		aplicarFiltros(filtro, sql, parametros);

		final List<PrestamoEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirPrestamoEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los préstamos por filtro.");
		}
		return resultados;
	}

	private void aplicarFiltros(final PrestamoEntidad filtro, final StringBuilder sql, final List<Object> parametros) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}
		if (!UtilObjeto.esNulo(filtro.getEjemplar()) && !UtilObjeto.esNulo(filtro.getEjemplar().getId())) {
			sql.append(" AND p.ejemplarId = ?");
			parametros.add(filtro.getEjemplar().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getUsuario()) && !UtilObjeto.esNulo(filtro.getUsuario().getId())) {
			sql.append(" AND p.usuarioId = ?");
			parametros.add(filtro.getUsuario().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getEstadoPrestamo()) && !UtilTexto.esNula(filtro.getEstadoPrestamo().getNombre())) {
			sql.append(" AND ep.nombre = ?");
			parametros.add(filtro.getEstadoPrestamo().getNombre());
		}
		if (!UtilObjeto.esNulo(filtro.getFechaPrestamo()) && !filtro.getFechaPrestamo().equals(UtilFecha.FECHA_DEFECTO)) {
			sql.append(" AND p.fechaPrestamo = ?");
			parametros.add(java.sql.Date.valueOf(filtro.getFechaPrestamo()));
		}
	}

	private PrestamoEntidad construirPrestamoEntidad(final ResultSet rs) throws SQLException {
		return new PrestamoEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.fechaPrestamo(rs.getDate("fechaPrestamo").toLocalDate())
				.fechaDevolucionEsperada(rs.getDate("fechaDevolucionEsperada").toLocalDate())
				.estadoPrestamo(new EstadoPrestamoEntidad.Builder()
						.id(UUID.fromString(rs.getString("estadoPrestamoId")))
						.nombre(rs.getString("estadoPrestamoNombre"))
						.build())
				.reserva(new ReservaEntidad.Builder()
						.id(UUID.fromString(rs.getString("reservaId")))
						.build())
				.usuario(new UsuarioEntidad.Builder()
						.id(UUID.fromString(rs.getString("usuarioId")))
						.build())
				.ejemplar(new EjemplarEntidad.Builder()
						.id(UUID.fromString(rs.getString("ejemplarId")))
						.libro(new LibroEntidad.Builder()
								.id(UUID.fromString(rs.getString("libroId")))
								.titulo(rs.getString("titulo"))
								.disponibles(rs.getInt("disponibles"))
								.tipoLibro(new TipoLibroEntidad.Builder()
										.id(UUID.fromString(rs.getString("tipoLibroId")))
										.nombre(rs.getString("tipoLibroNombre"))
										.build())
								.categoria(new CategoriaEntidad.Builder()
										.id(UUID.fromString(rs.getString("categoriaId")))
										.build())
								.editorial(new EditorialEntidad.Builder()
										.id(UUID.fromString(rs.getString("editorialId")))
										.build())
								.build())
						.build())
				.build();
	}

}
