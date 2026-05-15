package com.libreria.negocio.fachada.editorial.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EditorialDTO;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.casouso.editorial.ConsultarTodasEditorialesCasoUso;
import com.libreria.negocio.casouso.editorial.impl.ConsultarTodasEditorialesCasoUsoImpl;
import com.libreria.negocio.fachada.editorial.ConsultarTodasEditorialesFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodasEditorialesFachadaImpl implements ConsultarTodasEditorialesFachada {

	private final DAOFactory daoFactory;
	private final ConsultarTodasEditorialesCasoUso casoUso;

	public ConsultarTodasEditorialesFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ConsultarTodasEditorialesCasoUsoImpl(daoFactory);
	}

	@Override
	public List<EditorialDTO> ejecutar(final EditorialDTO filtro) {
		try {
			final EditorialDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new EditorialDTO.Builder().build());
			final EditorialEntidad filtroEntidad = new EditorialEntidad.Builder()
					.nit(filtroEfectivo.getNit())
					.nombre(filtroEfectivo.getNombre())
					.build();

			final List<EditorialEntidad> entidades = casoUso.ejecutar(filtroEntidad);
			final List<EditorialDTO> resultado = new ArrayList<>();
			for (final EditorialEntidad entidad : entidades) {
				resultado.add(new EditorialDTO.Builder()
						.id(entidad.getId())
						.nit(entidad.getNit())
						.nombre(entidad.getNombre())
						.build());
			}
			return resultado;

		} catch (GestorLibreriaExcepcion excepcion) {
			throw excepcion;

		} catch (Exception excepcion) {
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las editoriales.", "Error técnico inesperado en ConsultarTodasEditorialesFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
