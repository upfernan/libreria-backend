package com.libreria.negocio.fachada.pago.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.DevolucionDTO;
import com.libreria.dto.MultaDTO;
import com.libreria.dto.PagoDTO;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.dto.UsuarioDTO;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.pago.ConsultarPagoPorIdCasoUso;
import com.libreria.negocio.casouso.pago.impl.ConsultarPagoPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.pago.ConsultarPagoPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarPagoPorIdFachadaImpl implements ConsultarPagoPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarPagoPorIdCasoUso casoUso;

    public ConsultarPagoPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarPagoPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public PagoDTO ejecutar(final UUID id) {
        try {
            final PagoEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new PagoEntidad.Builder().build());
            return new PagoDTO.Builder()
                    .id(entidad.getId())
                    .fechaPago(entidad.getFechaPago())
                    .multa(new MultaDTO.Builder()
                            .id(entidad.getMulta().getId())
                            .montoTotal(entidad.getMulta().getMontoTotal())
                            .fechaGeneracion(entidad.getMulta().getFechaGeneracion())
                            .pagada(entidad.getMulta().getPagada())
                            .diasRetraso(entidad.getMulta().getDiasRetraso())
                            .tarifaMulta(new TarifaMultaDTO.Builder()
                                    .id(entidad.getMulta().getTarifaMulta().getId())
                                    .valorDiario(entidad.getMulta().getTarifaMulta().getValorDiario())
                                    .fechaInicioVigencia(entidad.getMulta().getTarifaMulta().getFechaInicioVigencia())
                                    .fechaFinVigencia(entidad.getMulta().getTarifaMulta().getFechaFinVigencia())
                                    .build())
                            .devolucion(new DevolucionDTO.Builder()
                                    .id(entidad.getMulta().getDevolucion().getId())
                                    .fechaDevolucion(entidad.getMulta().getDevolucion().getFechaDevolucion())
                                    .build())
                            .usuarioAfectado(new UsuarioDTO.Builder()
                                    .id(entidad.getMulta().getUsuarioAfectado().getId())
                                    .primerNombre(entidad.getMulta().getUsuarioAfectado().getPrimerNombre())
                                    .segundoNombre(entidad.getMulta().getUsuarioAfectado().getSegundoNombre())
                                    .primerApellido(entidad.getMulta().getUsuarioAfectado().getPrimerApellido())
                                    .segundoApellido(entidad.getMulta().getUsuarioAfectado().getSegundoApellido())
                                    .build())
                            .build())
                    .build();

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el pago.", "Error técnico inesperado en ConsultarPagoPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
