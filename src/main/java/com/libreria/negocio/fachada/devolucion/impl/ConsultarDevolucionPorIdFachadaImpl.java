package com.libreria.negocio.fachada.devolucion.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.DevolucionDTO;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.negocio.assembler.dto.impl.DevolucionDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.DevolucionEntidadAssembler;
import com.libreria.negocio.casouso.devolucion.ConsultarDevolucionPorIdCasoUso;
import com.libreria.negocio.casouso.devolucion.impl.ConsultarDevolucionPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.devolucion.ConsultarDevolucionPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarDevolucionPorIdFachadaImpl implements ConsultarDevolucionPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarDevolucionPorIdCasoUso casoUso;

    public ConsultarDevolucionPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarDevolucionPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public DevolucionDTO ejecutar(final UUID id) {
        try {
            final DevolucionEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new DevolucionEntidad.Builder().build());
            return DevolucionDTOAssembler.getInstance().ensamblarDTO(
                    DevolucionEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar la devolución.", "Error técnico inesperado en ConsultarDevolucionPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
