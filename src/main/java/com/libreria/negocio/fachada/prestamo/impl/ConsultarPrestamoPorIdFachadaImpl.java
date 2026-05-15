package com.libreria.negocio.fachada.prestamo.impl;

import java.util.UUID;

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
import com.libreria.negocio.casouso.prestamo.ConsultarPrestamoPorIdCasoUso;
import com.libreria.negocio.casouso.prestamo.impl.ConsultarPrestamoPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.prestamo.ConsultarPrestamoPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarPrestamoPorIdFachadaImpl implements ConsultarPrestamoPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarPrestamoPorIdCasoUso casoUso;

    public ConsultarPrestamoPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarPrestamoPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public PrestamoDTO ejecutar(final UUID id) {
        try {
            final PrestamoEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new PrestamoEntidad.Builder().build());
            return construirPrestamoDTO(entidad);

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el préstamo.", "Error técnico inesperado en ConsultarPrestamoPorIdFachadaImpl: " + excepcion.getMessage());

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
