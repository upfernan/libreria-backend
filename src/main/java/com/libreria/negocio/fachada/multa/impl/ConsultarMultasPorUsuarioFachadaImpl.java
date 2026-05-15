package com.libreria.negocio.fachada.multa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.DevolucionDTO;
import com.libreria.dto.MultaDTO;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.dto.UsuarioDTO;
import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.casouso.multa.ConsultarMultasPorUsuarioCasoUso;
import com.libreria.negocio.casouso.multa.impl.ConsultarMultasPorUsuarioCasoUsoImpl;
import com.libreria.negocio.fachada.multa.ConsultarMultasPorUsuarioFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarMultasPorUsuarioFachadaImpl implements ConsultarMultasPorUsuarioFachada {

    private final DAOFactory daoFactory;
    private final ConsultarMultasPorUsuarioCasoUso casoUso;

    public ConsultarMultasPorUsuarioFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarMultasPorUsuarioCasoUsoImpl(daoFactory);
    }

    @Override
    public List<MultaDTO> ejecutar(final UUID usuarioId) {
        try {
            final List<MultaEntidad> entidades = casoUso.ejecutar(usuarioId);
            final List<MultaDTO> resultado = new ArrayList<>();
            for (final MultaEntidad entidad : entidades) {
                resultado.add(new MultaDTO.Builder()
                        .id(entidad.getId())
                        .montoTotal(entidad.getMontoTotal())
                        .fechaGeneracion(entidad.getFechaGeneracion())
                        .pagada(entidad.getPagada())
                        .diasRetraso(entidad.getDiasRetraso())
                        .tarifaMulta(new TarifaMultaDTO.Builder()
                                .id(entidad.getTarifaMulta().getId())
                                .valorDiario(entidad.getTarifaMulta().getValorDiario())
                                .fechaInicioVigencia(entidad.getTarifaMulta().getFechaInicioVigencia())
                                .fechaFinVigencia(entidad.getTarifaMulta().getFechaFinVigencia())
                                .build())
                        .devolucion(new DevolucionDTO.Builder()
                                .id(entidad.getDevolucion().getId())
                                .fechaDevolucion(entidad.getDevolucion().getFechaDevolucion())
                                .build())
                        .usuarioAfectado(new UsuarioDTO.Builder()
                                .id(entidad.getUsuarioAfectado().getId())
                                .primerNombre(entidad.getUsuarioAfectado().getPrimerNombre())
                                .segundoNombre(entidad.getUsuarioAfectado().getSegundoNombre())
                                .primerApellido(entidad.getUsuarioAfectado().getPrimerApellido())
                                .segundoApellido(entidad.getUsuarioAfectado().getSegundoApellido())
                                .build())
                        .build());
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las multas del usuario.", "Error técnico inesperado en ConsultarMultasPorUsuarioFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
