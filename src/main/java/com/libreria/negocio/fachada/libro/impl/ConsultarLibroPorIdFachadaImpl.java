package com.libreria.negocio.fachada.libro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.CategoriaDTO;
import com.libreria.dto.EditorialDTO;
import com.libreria.dto.LibroDTO;
import com.libreria.dto.TipoLibroDTO;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.libro.ConsultarLibroPorIdCasoUso;
import com.libreria.negocio.casouso.libro.impl.ConsultarLibroPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.libro.ConsultarLibroPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarLibroPorIdFachadaImpl implements ConsultarLibroPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarLibroPorIdCasoUso casoUso;

    public ConsultarLibroPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarLibroPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public LibroDTO ejecutar(final UUID id) {
        try {
            final LibroEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new LibroEntidad.Builder().build());
            return new LibroDTO.Builder()
                    .id(entidad.getId())
                    .titulo(entidad.getTitulo())
                    .disponibles(entidad.getDisponibles())
                    .tipoLibro(new TipoLibroDTO.Builder()
                            .id(entidad.getTipoLibro().getId())
                            .nombre(entidad.getTipoLibro().getNombre())
                            .build())
                    .categoria(new CategoriaDTO.Builder()
                            .id(entidad.getCategoria().getId())
                            .nombre(entidad.getCategoria().getNombre())
                            .build())
                    .editorial(new EditorialDTO.Builder()
                            .id(entidad.getEditorial().getId())
                            .nombre(entidad.getEditorial().getNombre())
                            .build())
                    .build();

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el libro.", "Error técnico inesperado en ConsultarLibroPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
