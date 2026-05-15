package com.libreria.negocio.fachada.categoria.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.CategoriaDTO;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.negocio.casouso.categoria.ConsultarTodasCategoriasCasoUso;
import com.libreria.negocio.casouso.categoria.impl.ConsultarTodasCategoriasCasoUsoImpl;
import com.libreria.negocio.fachada.categoria.ConsultarTodasCategoriasFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodasCategoriasFachadaImpl implements ConsultarTodasCategoriasFachada {

	private final DAOFactory daoFactory;
	private final ConsultarTodasCategoriasCasoUso casoUso;

	public ConsultarTodasCategoriasFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ConsultarTodasCategoriasCasoUsoImpl(daoFactory);
	}

	@Override
	public List<CategoriaDTO> ejecutar(final CategoriaDTO filtro) {
		try {
			final CategoriaDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new CategoriaDTO.Builder().build());
			final CategoriaEntidad filtroEntidad = new CategoriaEntidad.Builder()
					.nombre(filtroEfectivo.getNombre())
					.descripcion(filtroEfectivo.getDescripcion())
					.build();

			final List<CategoriaEntidad> entidades = casoUso.ejecutar(filtroEntidad);
			final List<CategoriaDTO> resultado = new ArrayList<>();
			for (final CategoriaEntidad entidad : entidades) {
				resultado.add(new CategoriaDTO.Builder()
						.id(entidad.getId())
						.nombre(entidad.getNombre())
						.descripcion(entidad.getDescripcion())
						.build());
			}
			return resultado;

		} catch (GestorLibreriaExcepcion excepcion) {
			throw excepcion;

		} catch (Exception excepcion) {
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las categorías.", "Error técnico inesperado en ConsultarTodasCategoriasFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
