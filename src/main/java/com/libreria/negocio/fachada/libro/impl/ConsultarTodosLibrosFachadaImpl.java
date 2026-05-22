package com.libreria.negocio.fachada.libro.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.LibroDTO;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.assembler.dto.impl.LibroDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.LibroEntidadAssembler;
import com.libreria.negocio.casouso.libro.ConsultarTodosLibrosCasoUso;
import com.libreria.negocio.casouso.libro.impl.ConsultarTodosLibrosCasoUsoImpl;
import com.libreria.negocio.fachada.libro.ConsultarTodosLibrosFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosLibrosFachadaImpl implements ConsultarTodosLibrosFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosLibrosCasoUso casoUso;

    public ConsultarTodosLibrosFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosLibrosCasoUsoImpl(daoFactory);
    }

    @Override
    public List<LibroDTO> ejecutar(final LibroDTO filtro) {
        try {
            final LibroDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new LibroDTO.Builder().build());
            final LibroEntidad filtroEntidad = LibroEntidadAssembler.getInstance().ensamblarEntidad(
                    LibroDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<LibroEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<LibroDTO> resultado = new ArrayList<>();
            for (final LibroEntidad entidad : entidades) {
                resultado.add(LibroDTOAssembler.getInstance().ensamblarDTO(
                        LibroEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los libros.", "Error técnico inesperado en ConsultarTodosLibrosFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
