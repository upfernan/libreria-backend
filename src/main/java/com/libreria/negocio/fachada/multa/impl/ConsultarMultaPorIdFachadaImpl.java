package com.libreria.negocio.fachada.multa.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.DevolucionDTO;
import com.libreria.dto.MultaDTO;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.dto.UsuarioDTO;
import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.casouso.multa.ConsultarMultaPorIdCasoUso;
import com.libreria.negocio.casouso.multa.impl.ConsultarMultaPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.multa.ConsultarMultaPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarMultaPorIdFachadaImpl implements ConsultarMultaPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarMultaPorIdCasoUso casoUso;

    public ConsultarMultaPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarMultaPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public MultaDTO ejecutar(final UUID id) {
        try {
            final MultaEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new MultaEntidad.Builder().build());
            return new MultaDTO.Builder()
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
                    .build();

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar la multa.", "Error técnico inesperado en ConsultarMultaPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
