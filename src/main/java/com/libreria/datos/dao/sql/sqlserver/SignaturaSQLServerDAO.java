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

import com.libreria.datos.dao.SignaturaDAO;
import com.libreria.datos.dao.sql.SQLDAO;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class SignaturaSQLServerDAO extends SQLDAO implements SignaturaDAO {

	private static final Logger logger = LoggerFactory.getLogger(SignaturaSQLServerDAO.class);

	public SignaturaSQLServerDAO(final Connection conexion) {
		super(conexion);
	}

	@Override
	public void crear(final SignaturaEntidad entidad) {
		logger.debug("Entre al metodo crear de SignaturaSQLServerDAO...");
		final String sql = "INSERT INTO signatura (id, pasillo, estante, posicion) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, entidad.getId().toString());
			ps.setString(2, String.valueOf(entidad.getPasillo()));
			ps.setInt(3, entidad.getEstante());
			ps.setInt(4, entidad.getPosicion());
			ps.executeUpdate();
			logger.debug("Sali del metodo crear de SignaturaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible registrar la signatura.",
					"Se presento una SQLException al ejecutar INSERT en la tabla signatura desde SignaturaSQLServerDAO.crear.");
		}
	}

	@Override
	public void actualizar(final UUID id, final SignaturaEntidad entidad) {
		logger.debug("Entre al metodo actualizar de SignaturaSQLServerDAO...");
		final String sql = "UPDATE signatura SET pasillo = ?, estante = ?, posicion = ? WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, String.valueOf(entidad.getPasillo()));
			ps.setInt(2, entidad.getEstante());
			ps.setInt(3, entidad.getPosicion());
			ps.setString(4, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo actualizar de SignaturaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible actualizar la signatura.",
					"Se presento una SQLException al ejecutar UPDATE en la tabla signatura desde SignaturaSQLServerDAO.actualizar.");
		}
	}

	@Override
	public void eliminar(final UUID id) {
		logger.debug("Entre al metodo eliminar de SignaturaSQLServerDAO...");
		final String sql = "DELETE FROM signatura WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			ps.executeUpdate();
			logger.debug("Sali del metodo eliminar de SignaturaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible eliminar la signatura.",
					"Se presento una SQLException al ejecutar DELETE en la tabla signatura desde SignaturaSQLServerDAO.eliminar.");
		}
	}

	@Override
	public List<SignaturaEntidad> consultarTodos() {
		logger.debug("Entre al metodo consultarTodos de SignaturaSQLServerDAO...");
		final List<SignaturaEntidad> resultado = consultarPorFiltro(new SignaturaEntidad.Builder().build());
		logger.debug("Sali del metodo consultarTodos de SignaturaSQLServerDAO exitosamente.");
		return resultado;
	}

	@Override
	public SignaturaEntidad consultarPorId(final UUID id) {
		logger.debug("Entre al metodo consultarPorId de SignaturaSQLServerDAO...");
		final String sql = "SELECT id, pasillo, estante, posicion FROM signatura WHERE id = ?";
		try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, id.toString());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final SignaturaEntidad resultado = construirSignaturaEntidad(rs);
					logger.debug("Sali del metodo consultarPorId de SignaturaSQLServerDAO exitosamente.");
					return resultado;
				}
			}
			logger.debug("Sali del metodo consultarPorId de SignaturaSQLServerDAO exitosamente (sin resultados).");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar la signatura por identificador.",
					"Se presento una SQLException al ejecutar SELECT por id en la tabla signatura desde SignaturaSQLServerDAO.consultarPorId.");
		}
		return new SignaturaEntidad.Builder().build();
	}

	@Override
	public List<SignaturaEntidad> consultarPorFiltro(final SignaturaEntidad filtro) {
		logger.debug("Entre al metodo consultarPorFiltro de SignaturaSQLServerDAO...");
		final StringBuilder sql = new StringBuilder("SELECT id, pasillo, estante, posicion FROM signatura WHERE 1=1");
		final List<Object> parametros = new ArrayList<>();

		if (!UtilObjeto.esNulo(filtro)) {
			if (UtilUUID.tieneValor(filtro.getId())) {
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
			logger.debug("Sali del metodo consultarPorFiltro de SignaturaSQLServerDAO exitosamente.");
		} catch (SQLException e) {
			throw GestorLibreriaExcepcion.crear(e, "No fue posible consultar las signaturas por filtro.",
					"Se presento una SQLException al ejecutar SELECT por filtro en la tabla signatura desde SignaturaSQLServerDAO.consultarPorFiltro.");
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
