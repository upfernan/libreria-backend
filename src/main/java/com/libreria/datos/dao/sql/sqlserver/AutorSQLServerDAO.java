package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.AutorDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.AutorEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AutorSQLServerDAO extends SQLDAO implements AutorDAO {

	public AutorSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final AutorEntidad entidad) {
		final String sql = "INSERT INTO autor (id, primerNombre, segundoNombre, primerApellido, segundoApellido) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getPrimerNombre());
			ps.setString(3, entidad.getSegundoNombre());
			ps.setString(4, entidad.getPrimerApellido());
			ps.setString(5, entidad.getSegundoApellido());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar el autor.");
		}
	}

	@Override
	public void actualizar(final UUID id, final AutorEntidad entidad) {
		final String sql = "UPDATE autor SET primerNombre = ?, segundoNombre = ?, primerApellido = ?, segundoApellido = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getPrimerNombre());
			ps.setString(2, entidad.getSegundoNombre());
			ps.setString(3, entidad.getPrimerApellido());
			ps.setString(4, entidad.getSegundoApellido());
			ps.setString(5, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar el autor.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM autor WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar el autor.");
		}
	}

	@Override
	public List<AutorEntidad> consultarTodos() {
		final String sql = "SELECT id, primerNombre, segundoNombre, primerApellido, segundoApellido FROM autor";
		final List<AutorEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirAutorEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los autores.");
		}
		return resultados;
	}

	@Override
	public AutorEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, primerNombre, segundoNombre, primerApellido, segundoApellido FROM autor WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirAutorEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar el autor por identificador.");
		}
		return null;
	}

	@Override
	public List<AutorEntidad> consultarPorFiltro(final AutorEntidad filtro) {
		final StringBuilder sql = new StringBuilder("SELECT id, primerNombre, segundoNombre, primerApellido, segundoApellido FROM autor WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilTexto.esNula(filtro.getPrimerNombre())) {
				sql.append(" AND primerNombre = ?");
				parametros.add(filtro.getPrimerNombre());
			}
			if (!UtilTexto.esNula(filtro.getPrimerApellido())) {
				sql.append(" AND primerApellido = ?");
				parametros.add(filtro.getPrimerApellido());
			}
		}

		final List<AutorEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirAutorEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los autores por filtro.");
		}
		return resultados;
	}

	private AutorEntidad construirAutorEntidad(final ResultSet rs) throws SQLException {
		return new AutorEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.primerNombre(rs.getString("primerNombre"))
				.segundoNombre(rs.getString("segundoNombre"))
				.primerApellido(rs.getString("primerApellido"))
				.segundoApellido(rs.getString("segundoApellido"))
				.build();
	}

}
