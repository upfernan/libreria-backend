package com.libreria.negocio.fachada.estadoprestamo.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EstadoPrestamoDTO;
import com.libreria.entidad.EstadoPrestamoEntidad;
import com.libreria.negocio.assembler.dto.impl.EstadoPrestamoDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.EstadoPrestamoEntidadAssembler;
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
            final EstadoPrestamoEntidad filtroEntidad = EstadoPrestamoEntidadAssembler.getInstance().ensamblarEntidad(
                    EstadoPrestamoDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<EstadoPrestamoEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<EstadoPrestamoDTO> resultado = new ArrayList<>();
            for (final EstadoPrestamoEntidad entidad : entidades) {
                resultado.add(EstadoPrestamoDTOAssembler.getInstance().ensamblarDTO(
                        EstadoPrestamoEntidadAssembler.getInstance().ensamblarDominio(entidad)));
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
