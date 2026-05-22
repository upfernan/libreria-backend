package com.libreria.negocio.fachada.autorlibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorLibroDTO;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.assembler.dto.impl.AutorLibroDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.AutorLibroEntidadAssembler;
import com.libreria.negocio.casouso.autorlibro.ConsultarAutorLibroPorIdCasoUso;
import com.libreria.negocio.casouso.autorlibro.impl.ConsultarAutorLibroPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.autorlibro.ConsultarAutorLibroPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarAutorLibroPorIdFachadaImpl implements ConsultarAutorLibroPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarAutorLibroPorIdCasoUso casoUso;

    public ConsultarAutorLibroPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarAutorLibroPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public AutorLibroDTO ejecutar(final UUID id) {
        try {
            final AutorLibroEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new AutorLibroEntidad.Builder().build());
            return AutorLibroDTOAssembler.getInstance().ensamblarDTO(
                    AutorLibroEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar la relación autor-libro.", "Error técnico inesperado en ConsultarAutorLibroPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
