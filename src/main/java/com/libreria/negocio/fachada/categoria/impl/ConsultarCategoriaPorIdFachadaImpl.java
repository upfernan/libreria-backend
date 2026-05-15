package com.libreria.negocio.fachada.categoria.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.CategoriaDTO;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.negocio.casouso.categoria.ConsultarCategoriaPorIdCasoUso;
import com.libreria.negocio.casouso.categoria.impl.ConsultarCategoriaPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.categoria.ConsultarCategoriaPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;


public class ConsultarCategoriaPorIdFachadaImpl implements ConsultarCategoriaPorIdFachada {

	private final DAOFactory daoFactory;
	private final ConsultarCategoriaPorIdCasoUso casoUso;

	public ConsultarCategoriaPorIdFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ConsultarCategoriaPorIdCasoUsoImpl(daoFactory);
	}

	@Override
	public CategoriaDTO ejecutar(final UUID id) {
		try {
			final CategoriaEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new CategoriaEntidad.Builder().build());
			return new CategoriaDTO.Builder()
					.id(entidad.getId())
					.nombre(entidad.getNombre())
					.descripcion(entidad.getDescripcion())
					.build();

		} catch (GestorLibreriaExcepcion excepcion) {
			throw excepcion;

		} catch (Exception excepcion) {
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar la categoría.", "Error técnico inesperado en ConsultarCategoriaPorIdFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
