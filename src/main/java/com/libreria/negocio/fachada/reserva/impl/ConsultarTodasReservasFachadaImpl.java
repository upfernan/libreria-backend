package com.libreria.negocio.fachada.reserva.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EstadoReservaDTO;
import com.libreria.dto.LibroDTO;
import com.libreria.dto.ReservaDTO;
import com.libreria.dto.UsuarioDTO;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.casouso.reserva.ConsultarTodasReservasCasoUso;
import com.libreria.negocio.casouso.reserva.impl.ConsultarTodasReservasCasoUsoImpl;
import com.libreria.negocio.fachada.reserva.ConsultarTodasReservasFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodasReservasFachadaImpl implements ConsultarTodasReservasFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodasReservasCasoUso casoUso;

    public ConsultarTodasReservasFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodasReservasCasoUsoImpl(daoFactory);
    }

    @Override
    public List<ReservaDTO> ejecutar(final ReservaDTO filtro) {
        try {
            final ReservaDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new ReservaDTO.Builder().build());
            final ReservaEntidad filtroEntidad = new ReservaEntidad.Builder()
                    .usuario(filtroEfectivo.getUsuario() != null
                            ? new com.libreria.entidad.UsuarioEntidad.Builder()
                                    .id(filtroEfectivo.getUsuario().getId())
                                    .build()
                            : null)
                    .libro(filtroEfectivo.getLibro() != null
                            ? new com.libreria.entidad.LibroEntidad.Builder()
                                    .id(filtroEfectivo.getLibro().getId())
                                    .build()
                            : null)
                    .estadoReserva(filtroEfectivo.getEstadoReserva() != null
                            ? new com.libreria.entidad.EstadoReservaEntidad.Builder()
                                    .nombre(filtroEfectivo.getEstadoReserva().getNombre())
                                    .build()
                            : null)
                    .build();

            final List<ReservaEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<ReservaDTO> resultado = new ArrayList<>();
            for (final ReservaEntidad entidad : entidades) {
                resultado.add(new ReservaDTO.Builder()
                        .id(entidad.getId())
                        .fechaReserva(entidad.getFechaReserva())
                        .fechaExpiracion(entidad.getFechaExpiracion())
                        .estadoReserva(new EstadoReservaDTO.Builder()
                                .id(entidad.getEstadoReserva().getId())
                                .nombre(entidad.getEstadoReserva().getNombre())
                                .build())
                        .usuario(new UsuarioDTO.Builder()
                                .id(entidad.getUsuario().getId())
                                .build())
                        .libro(new LibroDTO.Builder()
                                .id(entidad.getLibro().getId())
                                .build())
                        .build());
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las reservas.", "Error técnico inesperado en ConsultarTodasReservasFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
