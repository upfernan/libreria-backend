package com.libreria.negocio.fachada.estadoreserva.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EstadoReservaDTO;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.negocio.casouso.estadoreserva.ConsultarTodosEstadosReservaCasoUso;
import com.libreria.negocio.casouso.estadoreserva.impl.ConsultarTodosEstadosReservaCasoUsoImpl;
import com.libreria.negocio.fachada.estadoreserva.ConsultarTodosEstadosReservaFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosEstadosReservaFachadaImpl implements ConsultarTodosEstadosReservaFachada {

	private final DAOFactory daoFactory;
	private final ConsultarTodosEstadosReservaCasoUso casoUso;

	public ConsultarTodosEstadosReservaFachadaImpl() {
		daoFactory = DAOFactory.getFactory();
		casoUso = new ConsultarTodosEstadosReservaCasoUsoImpl(daoFactory);
	}

	@Override
	public List<EstadoReservaDTO> ejecutar(final EstadoReservaDTO filtro) {
		try {
			final EstadoReservaDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new EstadoReservaDTO.Builder().build());
			final EstadoReservaEntidad filtroEntidad = new EstadoReservaEntidad.Builder()
					.nombre(filtroEfectivo.getNombre())
					.descripcion(filtroEfectivo.getDescripcion())
					.build();

			final List<EstadoReservaEntidad> entidades = casoUso.ejecutar(filtroEntidad);
			final List<EstadoReservaDTO> resultado = new ArrayList<>();
			for (final EstadoReservaEntidad entidad : entidades) {
				resultado.add(new EstadoReservaDTO.Builder()
						.id(entidad.getId())
						.nombre(entidad.getNombre())
						.descripcion(entidad.getDescripcion())
						.build());
			}
			return resultado;

		} catch (GestorLibreriaExcepcion excepcion) {
			throw excepcion;

		} catch (Exception excepcion) {
			throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los estados de reserva.", "Error técnico inesperado en ConsultarTodosEstadosReservaFachadaImpl: " + excepcion.getMessage());

		} finally {
			daoFactory.cerrarConexion();
		}
	}

}
