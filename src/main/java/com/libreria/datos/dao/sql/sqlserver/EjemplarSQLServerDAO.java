package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.EjemplarDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class EjemplarSQLServerDAO extends SQLDAO implements EjemplarDAO {

	public EjemplarSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final EjemplarEntidad entidad) {
		final String sql = "INSERT INTO ejemplar (id, libroId, signaturaId) VALUES (?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getLibro().getId().toString());
			final String signaturaId = !UtilObjeto.esNulo(entidad.getSignatura()) && !UtilObjeto.esNulo(entidad.getSignatura().getId())
					? entidad.getSignatura().getId().toString()
					: null;
			ps.setString(3, signaturaId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar el ejemplar.");
		}
	}

	@Override
	public void actualizar(final UUID id, final EjemplarEntidad entidad) {
		final String sql = "UPDATE ejemplar SET libroId = ?, signaturaId = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getLibro().getId().toString());
			final String signaturaId = !UtilObjeto.esNulo(entidad.getSignatura()) && !UtilObjeto.esNulo(entidad.getSignatura().getId())
					? entidad.getSignatura().getId().toString()
					: null;
			ps.setString(2, signaturaId);
			ps.setString(3, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar el ejemplar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM ejemplar WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar el ejemplar.");
		}
	}

	@Override
	public List<EjemplarEntidad> consultarTodos() {
		final String sql = "SELECT id, libroId, signaturaId FROM ejemplar";
		final List<EjemplarEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirEjemplarEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los ejemplares.");
		}
		return resultados;
	}

	@Override
	public EjemplarEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, libroId, signaturaId FROM ejemplar WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirEjemplarEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar el ejemplar por identificador.");
		}
		return null;
	}

	@Override
	public List<EjemplarEntidad> consultarPorFiltro(final EjemplarEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, libroId, signaturaId FROM ejemplar WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (!UtilObjeto.esNulo(filtro.getLibro()) && !UtilObjeto.esNulo(filtro.getLibro().getId())) {
				sql.append(" AND libroId = ?");
				parametros.add(filtro.getLibro().getId().toString());
			}
			if (!UtilObjeto.esNulo(filtro.getSignatura()) && !UtilObjeto.esNulo(filtro.getSignatura().getId())) {
				sql.append(" AND signaturaId = ?");
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
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los ejemplares por filtro.");
		}
		return resultados;
	}

	private EjemplarEntidad construirEjemplarEntidad(final ResultSet rs) throws SQLException {
		final String signaturaId = rs.getString("signaturaId");
		return new EjemplarEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.libro(new LibroEntidad.Builder()
						.id(UUID.fromString(rs.getString("libroId")))
						.build())
				.signatura(!UtilObjeto.esNulo(signaturaId)
						? new SignaturaEntidad.Builder().id(UUID.fromString(signaturaId)).build()
						: new SignaturaEntidad.Builder().build())
				.build();
	}

}
