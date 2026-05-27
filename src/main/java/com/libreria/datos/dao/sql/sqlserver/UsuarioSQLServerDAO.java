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

import com.libreria.datos.dao.UsuarioDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class UsuarioSQLServerDAO extends SQLDAO implements UsuarioDAO {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioSQLServerDAO.class);

	public UsuarioSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final UsuarioEntidad entidad) {
		logger.debug("Entre al metodo crear de UsuarioSQLServerDAO...");
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
			logger.debug("Sali del metodo crear de UsuarioSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar el usuario.",
					"Se presento una SQLException al ejecutar INSERT en la tabla usuario desde UsuarioSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final UsuarioEntidad entidad) {
		logger.debug("Entre al metodo actualizar de UsuarioSQLServerDAO...");
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
			logger.debug("Sali del metodo actualizar de UsuarioSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar el usuario.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla usuario desde UsuarioSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de UsuarioSQLServerDAO...");
		final String sql = "DELETE FROM usuario WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de UsuarioSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar el usuario.",
					"Se presento una SQLException al ejecutar DELETE en la tabla usuario desde UsuarioSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<UsuarioEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de UsuarioSQLServerDAO...");
		final List<UsuarioEntidad> resultado = consultarPorFiltro(new UsuarioEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de UsuarioSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public UsuarioEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de UsuarioSQLServerDAO...");
		final String sql = "SELECT id, tipoIdentificacionId, numeroIdentificacion, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico FROM usuario WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final UsuarioEntidad resultado = construirUsuarioEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de UsuarioSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de UsuarioSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar el usuario por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla usuario desde UsuarioSQLServerDAO.consultarPorId.");
		}
		return new UsuarioEntidad.Builder().build();
	}

	@Override
	public List<UsuarioEntidad> consultarPorFiltro(final UsuarioEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de UsuarioSQLServerDAO...");
		final StringBuilder sql = new StringBuilder(
				"SELECT id, tipoIdentificacionId, numeroIdentificacion, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico FROM usuario WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (UtilUUID.tieneValor(filtro.getId())) {
				sql.append(" AND id = ?");
				parametros.add(filtro.getId().toString());
			}
			if (UtilTexto.tieneContenido(filtro.getNumeroIdentificacion())) {
				sql.append(" AND numeroIdentificacion = ?");
				parametros.add(filtro.getNumeroIdentificacion());
			}
			if (!UtilObjeto.esNulo(filtro.getTipoIdentificacion()) && UtilUUID.tieneValor(filtro.getTipoIdentificacion().getId())) {
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
			logger.debug("Sali del metodo consultarPorFiltro de UsuarioSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar los usuarios por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla usuario desde UsuarioSQLServerDAO.consultarPorFiltro.");
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
