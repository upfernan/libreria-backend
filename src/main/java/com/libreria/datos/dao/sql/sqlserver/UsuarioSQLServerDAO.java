package com.libreria.datos.dao.sql.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.UsuarioDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class UsuarioSQLServerDAO extends SQLDAO implements UsuarioDAO {

	public UsuarioSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final UsuarioEntidad entidad) {
		final String sql = "INSERT INTO usuario (id, tipoIdentificacionId, numeroIdentificacion, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, entidad.getTipoIdentificacion().getId().toString());
			ps.setString(3, entidad.getNumeroIdentificacion());
			ps.setString(4, entidad.getPrimerNombre());
			ps.setString(5, entidad.getSegundoNombre());
			ps.setString(6, entidad.getPrimerApellido());
			ps.setString(7, entidad.getSegundoApellido());
			ps.setString(8, entidad.getCorreoElectronico());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible registrar el usuario.");
		}
	}

	@Override
	public void actualizar(final UUID id, final UsuarioEntidad entidad) {
		final String sql = "UPDATE usuario SET tipoIdentificacionId = ?, numeroIdentificacion = ?, primerNombre = ?, segundoNombre = ?, primerApellido = ?, segundoApellido = ?, correoElectronico = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getTipoIdentificacion().getId().toString());
			ps.setString(2, entidad.getNumeroIdentificacion());
			ps.setString(3, entidad.getPrimerNombre());
			ps.setString(4, entidad.getSegundoNombre());
			ps.setString(5, entidad.getPrimerApellido());
			ps.setString(6, entidad.getSegundoApellido());
			ps.setString(7, entidad.getCorreoElectronico());
			ps.setString(8, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible actualizar el usuario.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		final String sql = "DELETE FROM usuario WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible eliminar el usuario.");
		}
	}

	@Override
	public List<UsuarioEntidad> consultarTodos() {
		final String sql = "SELECT id, tipoIdentificacionId, numeroIdentificacion, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico FROM usuario";
		final List<UsuarioEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resultados.add(construirUsuarioEntidad(rs));
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los usuarios.");
		}
		return resultados;
	}

	@Override
	public UsuarioEntidad consultarPorId(final UUID id) {
		final String sql = "SELECT id, tipoIdentificacionId, numeroIdentificacion, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico FROM usuario WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return construirUsuarioEntidad(rs);
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar el usuario por identificador.");
		}
		return null;
	}

	@Override
	public List<UsuarioEntidad> consultarPorFiltro(final UsuarioEntidad filtro) {
		final StringBuilder sql = new StringBuilder(
				"SELECT id, tipoIdentificacionId, numeroIdentificacion, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico FROM usuario WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (!UtilObjeto.esNulo(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (!UtilTexto.esNula(filtro.getNumeroIdentificacion())) {
				sql.append(" AND numeroIdentificacion = ?");
				parametros.add(filtro.getNumeroIdentificacion());
			}
			if (!UtilObjeto.esNulo(filtro.getTipoIdentificacion()) && !UtilObjeto.esNulo(filtro.getTipoIdentificacion().getId())) {
				sql.append(" AND tipoIdentificacionId = ?");
				parametros.add(filtro.getTipoIdentificacion().getId().toString());
			}
		}

		final List<UsuarioEntidad> resultados = new ArrayList<>();
		try (PreparedStatement ps = getConexion().prepareStatement(sql.toString())) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					resultados.add(construirUsuarioEntidad(rs));
				}
			}
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear("No fue posible consultar los usuarios por filtro.");
		}
		return resultados;
	}

	private UsuarioEntidad construirUsuarioEntidad(final ResultSet rs) throws SQLException {
		return new UsuarioEntidad.Builder()
				.id(UUID.fromString(rs.getString("id")))
				.tipoIdentificacion(new TipoIdentificacionEntidad.Builder()
						.id(UUID.fromString(rs.getString("tipoIdentificacionId")))
						.build())
				.numeroIdentificacion(rs.getString("numeroIdentificacion"))
				.primerNombre(rs.getString("primerNombre"))
				.segundoNombre(rs.getString("segundoNombre"))
				.primerApellido(rs.getString("primerApellido"))
				.segundoApellido(rs.getString("segundoApellido"))
				.correoElectronico(rs.getString("correoElectronico"))
				.build();
	}

}
