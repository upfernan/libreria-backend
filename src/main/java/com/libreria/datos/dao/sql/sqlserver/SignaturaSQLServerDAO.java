package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.SignaturaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class SignaturaSQLServerDAO extends SQLDAO implements SignaturaDAO {

	public SignaturaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final SignaturaEntidad entidad) {
		final String sql = "INSERT INTO signatura (id, pasillo, estante, posicion) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, String.valueOf(entidad.getPasillo()));
			ps.setInt(3, entidad.getEstante());
			ps.setInt(4, entidad.getPosicion());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar la signatura.");
		}
	}

	@Override
	public void actualizar(final UUID id, final SignaturaEntidad entidad) {
		final String sql = "UPDATE signatura SET pasillo = ?, estante = ?, posicion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, String.valueOf(entidad.getPasillo()));
			ps.setInt(2, entidad.getEstante());
			ps.setInt(3, entidad.getPosicion());
			ps.setString(4, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar la signatura.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM signatura WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar la signatura.");
		}
	}

	@Override
	public List<SignaturaEntidad> consultarTodos() {
		final String sql = "SELECT id, pasillo, estante, posicion FROM signatura";
		final List<SignaturaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirSignaturaEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar las signaturas.");
		}
		return resultados;
	}

	@Override
	public SignaturaEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, pasillo, estante, posicion FROM signatura WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirSignaturaEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar la signatura por identificador.");
		}
		return null;
	}

	@Override
	public List<SignaturaEntidad> consultarPorFiltro(final SignaturaEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, pasillo, estante, posicion FROM signatura WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (filtro.getPasillo() != '\0' && filtro.getPasillo() != ' ') {
				sql.append(" AND pasillo = ? AND estante = ? AND posicion = ?");
				parametros.add(String.valueOf(filtro.getPasillo()));
				parametros.add(filtro.getEstante());
				parametros.add(filtro.getPosicion());
			}
		}

		final List<SignaturaEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirSignaturaEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar las signaturas por filtro.");
		}
		return resultados;
	}

	private SignaturaEntidad construirSignaturaEntidad(final ResultSet rs) throws SQLException {
		final String pasillo = rs.getString("pasillo");
		return new SignaturaEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.pasillo(!UtilObjeto.esNulo(pasillo) && !pasillo.isEmpty() ? pasillo.charAt(0) : ' ')
				.estante(rs.getInt("estante"))
				.posicion(rs.getInt("posicion"))
				.build();
	}

}
