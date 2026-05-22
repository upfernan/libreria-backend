package com.libreria.negocio.fachada.tipoidentificacion.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoIdentificacionDTO;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.negocio.assembler.dto.impl.TipoIdentificacionDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.TipoIdentificacionEntidadAssembler;
import com.libreria.negocio.casouso.tipoidentificacion.ConsultarTipoIdentificacionPorIdCasoUso;
import com.libreria.negocio.casouso.tipoidentificacion.impl.ConsultarTipoIdentificacionPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.tipoidentificacion.ConsultarTipoIdentificacionPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTipoIdentificacionPorIdFachadaImpl implements ConsultarTipoIdentificacionPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTipoIdentificacionPorIdCasoUso casoUso;

    public ConsultarTipoIdentificacionPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTipoIdentificacionPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public TipoIdentificacionDTO ejecutar(final UUID id) {
        try {
            final TipoIdentificacionEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new TipoIdentificacionEntidad.Builder().build());
            return TipoIdentificacionDTOAssembler.getInstance().ensamblarDTO(
                    TipoIdentificacionEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el tipo de identificación.", "Error técnico inesperado en ConsultarTipoIdentificacionPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
