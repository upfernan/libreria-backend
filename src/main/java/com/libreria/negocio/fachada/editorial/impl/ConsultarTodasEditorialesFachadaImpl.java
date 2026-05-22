package com.libreria.negocio.fachada.editorial.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EditorialDTO;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.assembler.dto.impl.EditorialDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.EditorialEntidadAssembler;
import com.libreria.negocio.casouso.editorial.ConsultarTodasEditorialesCasoUso;
import com.libreria.negocio.casouso.editorial.impl.ConsultarTodasEditorialesCasoUsoImpl;
import com.libreria.negocio.dominio.EditorialDominio;
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
			final EditorialEntidad filtroEntidad = EditorialEntidadAssembler.getInstance().ensamblarEntidad(
					EditorialDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

			final List<EditorialEntidad> entidades = casoUso.ejecutar(filtroEntidad);
			final List<EditorialDTO> resultado = new ArrayList<>();
			for (final EditorialEntidad entidad : entidades) {
				final EditorialDominio dominio = EditorialEntidadAssembler.getInstance().ensamblarDominio(entidad);
				resultado.add(EditorialDTOAssembler.getInstance().ensamblarDTO(dominio));
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
