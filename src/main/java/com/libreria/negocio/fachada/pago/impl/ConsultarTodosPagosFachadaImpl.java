package com.libreria.negocio.fachada.pago.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.DevolucionDTO;
import com.libreria.dto.MultaDTO;
import com.libreria.dto.PagoDTO;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.dto.UsuarioDTO;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.casouso.pago.ConsultarTodosPagosCasoUso;
import com.libreria.negocio.casouso.pago.impl.ConsultarTodosPagosCasoUsoImpl;
import com.libreria.negocio.fachada.pago.ConsultarTodosPagosFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosPagosFachadaImpl implements ConsultarTodosPagosFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosPagosCasoUso casoUso;

    public ConsultarTodosPagosFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosPagosCasoUsoImpl(daoFactory);
    }

    @Override
    public List<PagoDTO> ejecutar(final PagoDTO filtro) {
        try {
            final PagoDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new PagoDTO.Builder().build());
            final MultaDTO multaFiltro = UtilObjeto.obtenerValorDefecto(filtroEfectivo.getMulta(), new MultaDTO.Builder().build());

            final PagoEntidad filtroEntidad = new PagoEntidad.Builder()
                    .multa(new com.libreria.entidad.MultaEntidad.Builder()
                            .id(multaFiltro.getId())
                            .build())
                    .build();

            final List<PagoEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<PagoDTO> resultado = new ArrayList<>();
            for (final PagoEntidad entidad : entidades) {
                resultado.add(new PagoDTO.Builder()
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
                        .build());
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los pagos.", "Error técnico inesperado en ConsultarTodosPagosFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
