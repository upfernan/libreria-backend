package com.libreria.negocio.fachada.autorlibro.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorLibroDTO;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.assembler.dto.impl.AutorLibroDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.AutorLibroEntidadAssembler;
import com.libreria.negocio.casouso.autorlibro.ConsultarTodosAutoresLibroCasoUso;
import com.libreria.negocio.casouso.autorlibro.impl.ConsultarTodosAutoresLibroCasoUsoImpl;
import com.libreria.negocio.fachada.autorlibro.ConsultarTodosAutoresLibroFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosAutoresLibroFachadaImpl implements ConsultarTodosAutoresLibroFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosAutoresLibroCasoUso casoUso;

    public ConsultarTodosAutoresLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosAutoresLibroCasoUsoImpl(daoFactory);
    }

    @Override
    public List<AutorLibroDTO> ejecutar(final AutorLibroDTO filtro) {
        try {
            final List<AutorLibroEntidad> entidades = casoUso.ejecutar(new AutorLibroEntidad.Builder().build());
            final List<AutorLibroDTO> resultado = new ArrayList<>();
            for (final AutorLibroEntidad entidad : entidades) {
                resultado.add(AutorLibroDTOAssembler.getInstance().ensamblarDTO(
                        AutorLibroEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los autores-libro.", "Error técnico inesperado en ConsultarTodosAutoresLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
