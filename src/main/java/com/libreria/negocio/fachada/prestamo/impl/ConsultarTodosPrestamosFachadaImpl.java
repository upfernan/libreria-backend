package com.libreria.negocio.fachada.prestamo.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.CategoriaDTO;
import com.libreria.dto.EditorialDTO;
import com.libreria.dto.EjemplarDTO;
import com.libreria.dto.EstadoPrestamoDTO;
import com.libreria.dto.LibroDTO;
import com.libreria.dto.PrestamoDTO;
import com.libreria.dto.ReservaDTO;
import com.libreria.dto.TipoLibroDTO;
import com.libreria.dto.UsuarioDTO;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.prestamo.ConsultarTodosPrestamosCasoUso;
import com.libreria.negocio.casouso.prestamo.impl.ConsultarTodosPrestamosCasoUsoImpl;
import com.libreria.negocio.fachada.prestamo.ConsultarTodosPrestamosFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosPrestamosFachadaImpl implements ConsultarTodosPrestamosFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosPrestamosCasoUso casoUso;

    public ConsultarTodosPrestamosFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosPrestamosCasoUsoImpl(daoFactory);
    }

    @Override
    public List<PrestamoDTO> ejecutar(final PrestamoDTO filtro) {
        try {
            final PrestamoDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new PrestamoDTO.Builder().build());
            final PrestamoEntidad filtroEntidad = new PrestamoEntidad.Builder()
                    .usuario(filtroEfectivo.getUsuario() != null
                            ? new com.libreria.entidad.UsuarioEntidad.Builder()
                                    .id(filtroEfectivo.getUsuario().getId())
                                    .build()
                            : null)
                    .ejemplar(filtroEfectivo.getEjemplar() != null
                            ? new com.libreria.entidad.EjemplarEntidad.Builder()
                                    .id(filtroEfectivo.getEjemplar().getId())
                                    .build()
                            : null)
                    .estadoPrestamo(filtroEfectivo.getEstadoPrestamo() != null
                            ? new com.libreria.entidad.EstadoPrestamoEntidad.Builder()
                                    .nombre(filtroEfectivo.getEstadoPrestamo().getNombre())
                                    .build()
                            : null)
                    .fechaPrestamo(filtroEfectivo.getFechaPrestamo())
                    .build();

            final List<PrestamoEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<PrestamoDTO> resultado = new ArrayList<>();
            for (final PrestamoEntidad entidad : entidades) {
                resultado.add(construirPrestamoDTO(entidad));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los préstamos.", "Error técnico inesperado en ConsultarTodosPrestamosFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

    private PrestamoDTO construirPrestamoDTO(final PrestamoEntidad entidad) {
        return new PrestamoDTO.Builder()
                .id(entidad.getId())
                .fechaPrestamo(entidad.getFechaPrestamo())
                .fechaDevolucionEsperada(entidad.getFechaDevolucionEsperada())
                .estadoPrestamo(new EstadoPrestamoDTO.Builder()
                        .id(entidad.getEstadoPrestamo().getId())
                        .nombre(entidad.getEstadoPrestamo().getNombre())
                        .build())
                .reserva(new ReservaDTO.Builder()
                        .id(entidad.getReserva().getId())
                        .build())
                .usuario(new UsuarioDTO.Builder()
                        .id(entidad.getUsuario().getId())
                        .build())
                .ejemplar(new EjemplarDTO.Builder()
                        .id(entidad.getEjemplar().getId())
                        .libro(new LibroDTO.Builder()
                                .id(entidad.getEjemplar().getLibro().getId())
                                .titulo(entidad.getEjemplar().getLibro().getTitulo())
                                .disponibles(entidad.getEjemplar().getLibro().getDisponibles())
                                .tipoLibro(new TipoLibroDTO.Builder()
                                        .id(entidad.getEjemplar().getLibro().getTipoLibro().getId())
                                        .nombre(entidad.getEjemplar().getLibro().getTipoLibro().getNombre())
                                        .build())
                                .categoria(new CategoriaDTO.Builder()
                                        .id(entidad.getEjemplar().getLibro().getCategoria().getId())
                                        .build())
                                .editorial(new EditorialDTO.Builder()
                                        .id(entidad.getEjemplar().getLibro().getEditorial().getId())
                                        .build())
                                .build())
                        .build())
                .build();
    }

}
