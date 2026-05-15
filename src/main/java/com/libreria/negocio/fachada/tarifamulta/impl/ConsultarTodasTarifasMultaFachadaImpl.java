package com.libreria.negocio.fachada.tarifamulta.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.entidad.TarifaMultaEntidad;
import com.libreria.negocio.casouso.tarifamulta.ConsultarTodasTarifasMultaCasoUso;
import com.libreria.negocio.casouso.tarifamulta.impl.ConsultarTodasTarifasMultaCasoUsoImpl;
import com.libreria.negocio.fachada.tarifamulta.ConsultarTodasTarifasMultaFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodasTarifasMultaFachadaImpl implements ConsultarTodasTarifasMultaFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodasTarifasMultaCasoUso casoUso;

    public ConsultarTodasTarifasMultaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodasTarifasMultaCasoUsoImpl(daoFactory);
    }

    @Override
    public List<TarifaMultaDTO> ejecutar(final TarifaMultaDTO filtro) {
        try {
            final TarifaMultaDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new TarifaMultaDTO.Builder().build());
            final TarifaMultaEntidad filtroEntidad = new TarifaMultaEntidad.Builder()
                    .valorDiario(filtroEfectivo.getValorDiario())
                    .fechaInicioVigencia(filtroEfectivo.getFechaInicioVigencia())
                    .fechaFinVigencia(filtroEfectivo.getFechaFinVigencia())
                    .build();

            final List<TarifaMultaEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<TarifaMultaDTO> resultado = new ArrayList<>();
            for (final TarifaMultaEntidad entidad : entidades) {
                resultado.add(new TarifaMultaDTO.Builder()
                        .id(entidad.getId())
                        .valorDiario(entidad.getValorDiario())
                        .fechaInicioVigencia(entidad.getFechaInicioVigencia())
                        .fechaFinVigencia(entidad.getFechaFinVigencia())
                        .build());
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las tarifas de multa.", "Error técnico inesperado en ConsultarTodasTarifasMultaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
