package com.libreria.negocio.fachada.tipoidentificacion.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoIdentificacionDTO;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.negocio.assembler.dto.impl.TipoIdentificacionDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.TipoIdentificacionEntidadAssembler;
import com.libreria.negocio.casouso.tipoidentificacion.ConsultarTodosTiposIdentificacionCasoUso;
import com.libreria.negocio.casouso.tipoidentificacion.impl.ConsultarTodosTiposIdentificacionCasoUsoImpl;
import com.libreria.negocio.fachada.tipoidentificacion.ConsultarTodosTiposIdentificacionFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosTiposIdentificacionFachadaImpl implements ConsultarTodosTiposIdentificacionFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosTiposIdentificacionCasoUso casoUso;

    public ConsultarTodosTiposIdentificacionFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosTiposIdentificacionCasoUsoImpl(daoFactory);
    }

    @Override
    public List<TipoIdentificacionDTO> ejecutar(final TipoIdentificacionDTO filtro) {
        try {
            final TipoIdentificacionDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new TipoIdentificacionDTO.Builder().build());
            final TipoIdentificacionEntidad filtroEntidad = TipoIdentificacionEntidadAssembler.getInstance().ensamblarEntidad(
                    TipoIdentificacionDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<TipoIdentificacionEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<TipoIdentificacionDTO> resultado = new ArrayList<>();
            for (final TipoIdentificacionEntidad entidad : entidades) {
                resultado.add(TipoIdentificacionDTOAssembler.getInstance().ensamblarDTO(
                        TipoIdentificacionEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los tipos de identificación.", "Error técnico inesperado en ConsultarTodosTiposIdentificacionFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
