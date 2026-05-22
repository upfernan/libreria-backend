package com.libreria.negocio.fachada.tipolibro.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoLibroDTO;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.assembler.dto.impl.TipoLibroDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.TipoLibroEntidadAssembler;
import com.libreria.negocio.casouso.tipolibro.ConsultarTodosTiposLibroCasoUso;
import com.libreria.negocio.casouso.tipolibro.impl.ConsultarTodosTiposLibroCasoUsoImpl;
import com.libreria.negocio.fachada.tipolibro.ConsultarTodosTiposLibroFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosTiposLibroFachadaImpl implements ConsultarTodosTiposLibroFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosTiposLibroCasoUso casoUso;

    public ConsultarTodosTiposLibroFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosTiposLibroCasoUsoImpl(daoFactory);
    }

    @Override
    public List<TipoLibroDTO> ejecutar(final TipoLibroDTO filtro) {
        try {
            final TipoLibroDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new TipoLibroDTO.Builder().build());
            final TipoLibroEntidad filtroEntidad = TipoLibroEntidadAssembler.getInstance().ensamblarEntidad(
                    TipoLibroDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<TipoLibroEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<TipoLibroDTO> resultado = new ArrayList<>();
            for (final TipoLibroEntidad entidad : entidades) {
                resultado.add(TipoLibroDTOAssembler.getInstance().ensamblarDTO(
                        TipoLibroEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los tipos de libro.", "Error técnico inesperado en ConsultarTodosTiposLibroFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
