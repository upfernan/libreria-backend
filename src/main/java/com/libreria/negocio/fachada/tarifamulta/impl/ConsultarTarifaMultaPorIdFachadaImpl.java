package com.libreria.negocio.fachada.tarifamulta.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.assembler.dto.impl.TarifaMultaDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.TarifaMultaEntidadAssembler;
import com.libreria.negocio.casouso.tarifamulta.ConsultarTarifaMultaPorIdCasoUso;
import com.libreria.negocio.casouso.tarifamulta.impl.ConsultarTarifaMultaPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.tarifamulta.ConsultarTarifaMultaPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTarifaMultaPorIdFachadaImpl implements ConsultarTarifaMultaPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTarifaMultaPorIdCasoUso casoUso;

    public ConsultarTarifaMultaPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTarifaMultaPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public TarifaMultaDTO ejecutar(final UUID id) {
        try {
            final TarifaMultaEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new TarifaMultaEntidad.Builder().build());
            return TarifaMultaDTOAssembler.getInstance().ensamblarDTO(
                    TarifaMultaEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar la tarifa de multa.", "Error técnico inesperado en ConsultarTarifaMultaPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
