package com.libreria.negocio.fachada.editorial.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EditorialDTO;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.casouso.editorial.ConsultarEditorialPorIdCasoUso;
import com.libreria.negocio.casouso.editorial.impl.ConsultarEditorialPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.editorial.ConsultarEditorialPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarEditorialPorIdFachadaImpl implements ConsultarEditorialPorIdFachada {

	private final DAOFactory daoFactory;
	private final ConsultarEditorialPorIdCasoUso casoUso;

	public ConsultarEditorialPorIdFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ConsultarEditorialPorIdCasoUsoImpl(daoFactory);
	}

	@Override
	public EditorialDTO ejecutar(final UUID id) {
		try {
			final EditorialEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new EditorialEntidad.Builder().build());
			return new EditorialDTO.Builder()
					.id(entidad.getId())
					.nit(entidad.getNit())
					.nombre(entidad.getNombre())
					.build();

		} catch (GestorLibreriaExcepcion excepcion) {
			throw excepcion;

		} catch (Exception excepcion) {
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar la editorial.", "Error técnico inesperado en ConsultarEditorialPorIdFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
