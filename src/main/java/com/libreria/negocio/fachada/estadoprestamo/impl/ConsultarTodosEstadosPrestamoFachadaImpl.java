package com.libreria.negocio.fachada.estadoprestamo.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EstadoPrestamoDTO;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.negocio.casouso.estadoprestamo.ConsultarTodosEstadosPrestamoCasoUso;
import com.libreria.negocio.casouso.estadoprestamo.impl.ConsultarTodosEstadosPrestamoCasoUsoImpl;
import com.libreria.negocio.fachada.estadoprestamo.ConsultarTodosEstadosPrestamoFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosEstadosPrestamoFachadaImpl implements ConsultarTodosEstadosPrestamoFachada {

	private final DAOFactory daoFactory;
	private final ConsultarTodosEstadosPrestamoCasoUso casoUso;

	public ConsultarTodosEstadosPrestamoFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ConsultarTodosEstadosPrestamoCasoUsoImpl(daoFactory);
	}

	@Override
	public List<EstadoPrestamoDTO> ejecutar(final EstadoPrestamoDTO filtro) {
		try {
			final EstadoPrestamoDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new EstadoPrestamoDTO.Builder().build());
			final EstadoPrestamoEntidad filtroEntidad = new EstadoPrestamoEntidad.Builder()
					.nombre(filtroEfectivo.getNombre())
					.descripcion(filtroEfectivo.getDescripcion())
					.build();

			final List<EstadoPrestamoEntidad> entidades = casoUso.ejecutar(filtroEntidad);
			final List<EstadoPrestamoDTO> resultado = new ArrayList<>();
			for (final EstadoPrestamoEntidad entidad : entidades) {
				resultado.add(new EstadoPrestamoDTO.Builder()
						.id(entidad.getId())
						.nombre(entidad.getNombre())
						.descripcion(entidad.getDescripcion())
						.build());
			}
			return resultado;

		} catch (GestorLibreriaExcepcion excepcion) {
			throw excepcion;

		} catch (Exception excepcion) {
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los estados de préstamo.", "Error técnico inesperado en ConsultarTodosEstadosPrestamoFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
