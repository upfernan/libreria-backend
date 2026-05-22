package com.libreria.negocio.fachada.devolucion.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.DevolucionDTO;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.negocio.assembler.dto.impl.DevolucionDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.DevolucionEntidadAssembler;
import com.libreria.negocio.casouso.devolucion.ConsultarTodasDevolucionesCasoUso;
import com.libreria.negocio.casouso.devolucion.impl.ConsultarTodasDevolucionesCasoUsoImpl;
import com.libreria.negocio.fachada.devolucion.ConsultarTodasDevolucionesFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodasDevolucionesFachadaImpl implements ConsultarTodasDevolucionesFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodasDevolucionesCasoUso casoUso;

    public ConsultarTodasDevolucionesFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodasDevolucionesCasoUsoImpl(daoFactory);
    }

    @Override
    public List<DevolucionDTO> ejecutar(final DevolucionDTO filtro) {
        try {
            final DevolucionDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new DevolucionDTO.Builder().build());
            final DevolucionEntidad filtroEntidad = DevolucionEntidadAssembler.getInstance().ensamblarEntidad(
                    DevolucionDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<DevolucionEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<DevolucionDTO> resultado = new ArrayList<>();
            for (final DevolucionEntidad entidad : entidades) {
                resultado.add(DevolucionDTOAssembler.getInstance().ensamblarDTO(
                        DevolucionEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las devoluciones.", "Error técnico inesperado en ConsultarTodasDevolucionesFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
