package com.libreria.negocio.fachada.pago.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.PagoDTO;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.assembler.dto.impl.PagoDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.PagoEntidadAssembler;
import com.libreria.negocio.casouso.pago.ConsultarPagoPorIdCasoUso;
import com.libreria.negocio.casouso.pago.impl.ConsultarPagoPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.pago.ConsultarPagoPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarPagoPorIdFachadaImpl implements ConsultarPagoPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarPagoPorIdCasoUso casoUso;

    public ConsultarPagoPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarPagoPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public PagoDTO ejecutar(final UUID id) {
        try {
            final PagoEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new PagoEntidad.Builder().build());
            return PagoDTOAssembler.getInstance().ensamblarDTO(
                    PagoEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el pago.", "Error técnico inesperado en ConsultarPagoPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
