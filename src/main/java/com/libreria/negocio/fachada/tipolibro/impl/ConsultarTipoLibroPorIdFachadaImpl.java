package com.libreria.negocio.fachada.tipolibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoLibroDTO;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.assembler.dto.impl.TipoLibroDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.TipoLibroEntidadAssembler;
import com.libreria.negocio.casouso.tipolibro.ConsultarTipoLibroPorIdCasoUso;
import com.libreria.negocio.casouso.tipolibro.impl.ConsultarTipoLibroPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.tipolibro.ConsultarTipoLibroPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTipoLibroPorIdFachadaImpl implements ConsultarTipoLibroPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTipoLibroPorIdCasoUso casoUso;

    public ConsultarTipoLibroPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTipoLibroPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public TipoLibroDTO ejecutar(final UUID id) {
        try {
            final TipoLibroEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new TipoLibroEntidad.Builder().build());
            return TipoLibroDTOAssembler.getInstance().ensamblarDTO(
                    TipoLibroEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el tipo de libro.", "Error técnico inesperado en ConsultarTipoLibroPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
