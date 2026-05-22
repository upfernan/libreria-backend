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
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class PrestamoSQLServerDAO extends SQLDAO implements PrestamoDAO {

	private static final String SELECT_BASE =
			"SELECT p.id, p.fechaPrestamo, p.fechaDevolucionEsperada,"
			+ " p.estadoPrestamoId, ep.nombre AS estadoPrestamoNombre,"
			+ " p.reservaId,"
			+ " r.fechaReserva, r.fechaExpiracion,"
			+ " r.estadoReservaId, er.nombre AS estadoReservaNombre,"
			+ " r.libroId AS reservaLibroId,"
			+ " rl.tipoLibroId AS rlTipoLibroId,"
			+ " rl.categoriaId AS rlCategoriaId,"
			+ " rl.editorialId AS rlEditorialId,"
			+ " r.usuarioId AS reservaUsuarioId,"
			+ " ru.tipoIdentificacionId AS ruTipoIdentificacionId,"
			+ " p.usuarioId, u.tipoIdentificacionId AS pTipoIdentificacionId,"
			+ " u.primerNombre AS usuarioPrimerNombre, u.primerApellido AS usuarioPrimerApellido,"
			+ " p.ejemplarId, e.libroId, e.signaturaId,"
			+ " s.pasillo AS sigPasillo, s.estante AS sigEstante, s.posicion AS sigPosicion,"
			+ " l.titulo AS ejTitulo, l.disponibles AS ejDisponibles,"
			+ " l.tipoLibroId AS ejTipoLibroId,"
			+ " l.categoriaId AS ejCategoriaId,"
			+ " l.editorialId AS ejEditorialId,"
			+ " tl.nombre AS tipoLibroNombre"
			+ " FROM prestamo p"
			+ " LEFT JOIN estadoprestamo ep ON p.estadoPrestamoId = ep.id"
			+ " LEFT JOIN reserva r ON p.reservaId = r.id"
			+ " LEFT JOIN estadoreserva er ON r.estadoReservaId = er.id"
			+ " LEFT JOIN libro rl ON r.libroId = rl.id"
			+ " LEFT JOIN usuario ru ON r.usuarioId = ru.id"
			+ " LEFT JOIN usuario u ON p.usuarioId = u.id"
			+ " LEFT JOIN ejemplar e ON p.ejemplarId = e.id"
			+ " LEFT JOIN signatura s ON e.signaturaId = s.id"
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
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el préstamo.");
		}
	}

	@Override
	public void actualizar(final UUID id, final PrestamoEntidad entidad) {
		final String sql = "UPDATE prestamo SET estadoPrestamoId = ?, fechaDevolucionEsperada = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getEstadoPrestamo().getId().toString());
			ps.setDate(2, java.sql.Date.valueOf(entidad.getFechaDevolucionEsperada()));
			ps.setString(3, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el préstamo.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM prestamo WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el préstamo.");
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
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los préstamos.");
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
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el préstamo por identificador.");
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
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los préstamos por filtro.");
		}
		return resultados;
	}

	private void aplicarFiltros(final PrestamoEntidad filtro, final StringBuilder sql, final List<Object> parametros) {
		if (UtilObjeto.esNulo(filtro)) {
			return;
		}
		if (!UtilObjeto.esNulo(filtro.getEjemplar()) && UtilUUID.tieneValor(filtro.getEjemplar().getId())) {
			sql.append(" AND p.ejemplarId = ?");
			parametros.add(filtro.getEjemplar().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getUsuario()) && UtilUUID.tieneValor(filtro.getUsuario().getId())) {
			sql.append(" AND p.usuarioId = ?");
			parametros.add(filtro.getUsuario().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getEstadoPrestamo()) && UtilUUID.tieneValor(filtro.getEstadoPrestamo().getId())) {
			sql.append(" AND p.estadoPrestamoId = ?");
			parametros.add(filtro.getEstadoPrestamo().getId().toString());
		}
		if (!UtilObjeto.esNulo(filtro.getEstadoPrestamo()) && UtilTexto.tieneContenido(filtro.getEstadoPrestamo().getNombre())) {
			sql.append(" AND ep.nombre = ?");
			parametros.add(filtro.getEstadoPrestamo().getNombre());
		}
		if (!UtilObjeto.esNulo(filtro.getFechaPrestamo()) && !filtro.getFechaPrestamo().equals(UtilFecha.FECHA_DEFECTO)) {
			sql.append(" AND p.fechaPrestamo = ?");
			parametros.add(java.sql.Date.valueOf(filtro.getFechaPrestamo()));
		}
	}

	private PrestamoEntidad construirPrestamoEntidad(final ResultSet rs) throws SQLException {
		final String signaturaId = rs.getString("signaturaId");
		final String estadoReservaId = rs.getString("estadoReservaId");
		final String reservaLibroId = rs.getString("reservaLibroId");
		final String rlTipoLibroId = rs.getString("rlTipoLibroId");
		final String rlCategoriaId = rs.getString("rlCategoriaId");
		final String rlEditorialId = rs.getString("rlEditorialId");
		final String reservaUsuarioId = rs.getString("reservaUsuarioId");
		final String ruTipoIdentificacionId = rs.getString("ruTipoIdentificacionId");
		final String tipoIdentificacionId = rs.getString("pTipoIdentificacionId");
		final java.sql.Date fechaReservaSql = rs.getDate("fechaReserva");
		final java.sql.Date fechaExpiracionSql = rs.getDate("fechaExpiracion");

		final EstadoReservaEntidad estadoReserva = UtilObjeto.esNulo(estadoReservaId) ? null
				: new EstadoReservaEntidad.Builder()
						.id(UUID.fromString(estadoReservaId))
						.nombre(rs.getString("estadoReservaNombre"))
						.build();

		final TipoLibroEntidad tipoLibroReserva = UtilObjeto.esNulo(rlTipoLibroId) ? null
				: new TipoLibroEntidad.Builder().id(UUID.fromString(rlTipoLibroId)).build();

		final CategoriaEntidad categoriaReserva = UtilObjeto.esNulo(rlCategoriaId) ? null
				: new CategoriaEntidad.Builder().id(UUID.fromString(rlCategoriaId)).build();

		final EditorialEntidad editorialReserva = UtilObjeto.esNulo(rlEditorialId) ? null
				: new EditorialEntidad.Builder().id(UUID.fromString(rlEditorialId)).build();

		final LibroEntidad libroReserva = UtilObjeto.esNulo(reservaLibroId) ? null
				: new LibroEntidad.Builder()
						.id(UUID.fromString(reservaLibroId))
						.tipoLibro(tipoLibroReserva)
						.categoria(categoriaReserva)
						.editorial(editorialReserva)
						.build();

		final TipoIdentificacionEntidad tipoIdUsuarioReserva = UtilObjeto.esNulo(ruTipoIdentificacionId) ? null
				: new TipoIdentificacionEntidad.Builder().id(UUID.fromString(ruTipoIdentificacionId)).build();

		final UsuarioEntidad usuarioReserva = UtilObjeto.esNulo(reservaUsuarioId) ? null
				: new UsuarioEntidad.Builder()
						.id(UUID.fromString(reservaUsuarioId))
						.tipoIdentificacion(tipoIdUsuarioReserva)
						.build();

		final TipoIdentificacionEntidad tipoIdUsuarioPrestamo = UtilObjeto.esNulo(tipoIdentificacionId) ? null
				: new TipoIdentificacionEntidad.Builder().id(UUID.fromString(tipoIdentificacionId)).build();

		final java.time.LocalDate fechaReservaLocal = UtilObjeto.esNulo(fechaReservaSql) ? null : fechaReservaSql.toLocalDate();
		final java.time.LocalDate fechaExpiracionLocal = UtilObjeto.esNulo(fechaExpiracionSql) ? null : fechaExpiracionSql.toLocalDate();

		final char sigPasillo = UtilTexto.tieneContenido(rs.getString("sigPasillo")) ? rs.getString("sigPasillo").charAt(0) : '\0';

		final SignaturaEntidad signatura = UtilObjeto.esNulo(signaturaId)
				? new SignaturaEntidad.Builder().build()
				: new SignaturaEntidad.Builder()
						.id(UUID.fromString(signaturaId))
						.pasillo(sigPasillo)
						.estante(rs.getInt("sigEstante"))
						.posicion(rs.getInt("sigPosicion"))
						.build();

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
						.fechaReserva(fechaReservaLocal)
						.fechaExpiracion(fechaExpiracionLocal)
						.estadoReserva(estadoReserva)
						.libro(libroReserva)
						.usuario(usuarioReserva)
						.build())
				.usuario(new UsuarioEntidad.Builder()
						.id(UUID.fromString(rs.getString("usuarioId")))
						.primerNombre(rs.getString("usuarioPrimerNombre"))
						.primerApellido(rs.getString("usuarioPrimerApellido"))
						.tipoIdentificacion(tipoIdUsuarioPrestamo)
						.build())
				.ejemplar(new EjemplarEntidad.Builder()
						.id(UUID.fromString(rs.getString("ejemplarId")))
						.signatura(signatura)
						.libro(new LibroEntidad.Builder()
								.id(UUID.fromString(rs.getString("libroId")))
								.titulo(rs.getString("ejTitulo"))
								.disponibles(rs.getInt("ejDisponibles"))
								.tipoLibro(new TipoLibroEntidad.Builder()
										.id(UUID.fromString(rs.getString("ejTipoLibroId")))
										.nombre(rs.getString("tipoLibroNombre"))
										.build())
								.categoria(new CategoriaEntidad.Builder()
										.id(UUID.fromString(rs.getString("ejCategoriaId")))
										.build())
								.editorial(new EditorialEntidad.Builder()
										.id(UUID.fromString(rs.getString("ejEditorialId")))
										.build())
								.build())
						.build())
				.build();
	}

}
