package com.libreria.negocio.fachada.multa.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.MultaDTO;
import com.libreria.entidad.MultaEntidad;
import com.libreria.negocio.assembler.dto.impl.MultaDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.MultaEntidadAssembler;
import com.libreria.negocio.casouso.multa.ConsultarTodasMultasCasoUso;
import com.libreria.negocio.casouso.multa.impl.ConsultarTodasMultasCasoUsoImpl;
import com.libreria.negocio.fachada.multa.ConsultarTodasMultasFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodasMultasFachadaImpl implements ConsultarTodasMultasFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodasMultasCasoUso casoUso;

    public ConsultarTodasMultasFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodasMultasCasoUsoImpl(daoFactory);
    }

    @Override
    public List<MultaDTO> ejecutar(final MultaDTO filtro) {
        try {
            final List<MultaEntidad> entidades = casoUso.ejecutar(new MultaEntidad.Builder().build());
            final List<MultaDTO> resultado = new ArrayList<>();
            for (final MultaEntidad entidad : entidades) {
                resultado.add(MultaDTOAssembler.getInstance().ensamblarDTO(
                        MultaEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las multas.", "Error técnico inesperado en ConsultarTodasMultasFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
