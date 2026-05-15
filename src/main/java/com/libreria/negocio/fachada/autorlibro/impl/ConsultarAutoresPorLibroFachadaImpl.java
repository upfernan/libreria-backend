package com.libreria.negocio.fachada.autorlibro.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.dto.AutorLibroDTO;
import com.libreria.dto.LibroDTO;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.autorlibro.ConsultarAutoresPorLibroCasoUso;
import com.libreria.negocio.casouso.autorlibro.impl.ConsultarAutoresPorLibroCasoUsoImpl;
import com.libreria.negocio.fachada.autorlibro.ConsultarAutoresPorLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarAutoresPorLibroFachadaImpl implements ConsultarAutoresPorLibroFachada {

    private final DAOFactory daoFactory;
    private final ConsultarAutoresPorLibroCasoUso casoUso;

    public ConsultarAutoresPorLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarAutoresPorLibroCasoUsoImpl(daoFactory);
    }

    @Override
    public List<AutorLibroDTO> ejecutar(final UUID libroId) {
        try {
            final List<AutorLibroEntidad> entidades = casoUso.ejecutar(libroId);
            final List<AutorLibroDTO> resultado = new ArrayList<>();
            for (final AutorLibroEntidad entidad : entidades) {
                resultado.add(new AutorLibroDTO.Builder()
                        .id(entidad.getId())
                        .autor(new AutorDTO.Builder()
                                .id(entidad.getAutor().getId())
                                .primerNombre(entidad.getAutor().getPrimerNombre())
                                .segundoNombre(entidad.getAutor().getSegundoNombre())
                                .primerApellido(entidad.getAutor().getPrimerApellido())
                                .segundoApellido(entidad.getAutor().getSegundoApellido())
                                .build())
                        .libro(new LibroDTO.Builder()
                                .id(entidad.getLibro().getId())
                                .titulo(entidad.getLibro().getTitulo())
                                .build())
                        .build());
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los autores del libro.", "Error técnico inesperado en ConsultarAutoresPorLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
