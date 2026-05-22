package com.libreria.negocio.fachada.prestamo.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.PrestamoDTO;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.assembler.dto.impl.PrestamoDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.PrestamoEntidadAssembler;
import com.libreria.negocio.casouso.prestamo.ConsultarTodosPrestamosCasoUso;
import com.libreria.negocio.casouso.prestamo.impl.ConsultarTodosPrestamosCasoUsoImpl;
import com.libreria.negocio.fachada.prestamo.ConsultarTodosPrestamosFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosPrestamosFachadaImpl implements ConsultarTodosPrestamosFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosPrestamosCasoUso casoUso;

    public ConsultarTodosPrestamosFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosPrestamosCasoUsoImpl(daoFactory);
    }

    @Override
    public List<PrestamoDTO> ejecutar(final PrestamoDTO filtro) {
        try {
            final PrestamoDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new PrestamoDTO.Builder().build());
            final PrestamoEntidad filtroEntidad = PrestamoEntidadAssembler.getInstance().ensamblarEntidad(
                    PrestamoDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<PrestamoEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<PrestamoDTO> resultado = new ArrayList<>();
            for (final PrestamoEntidad entidad : entidades) {
                resultado.add(PrestamoDTOAssembler.getInstance().ensamblarDTO(
                        PrestamoEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los préstamos.", "Error técnico inesperado en ConsultarTodosPrestamosFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
