package com.libreria.negocio.fachada.libro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.LibroDTO;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.assembler.dto.impl.LibroDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.LibroEntidadAssembler;
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
            return LibroDTOAssembler.getInstance().ensamblarDTO(
                    LibroEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el libro.", "Error técnico inesperado en ConsultarLibroPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
