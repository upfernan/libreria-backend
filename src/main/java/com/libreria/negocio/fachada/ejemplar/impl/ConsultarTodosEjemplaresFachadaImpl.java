package com.libreria.negocio.fachada.ejemplar.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EjemplarDTO;
import com.libreria.dto.LibroDTO;
import com.libreria.dto.SignaturaDTO;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.ejemplar.ConsultarTodosEjemplaresCasoUso;
import com.libreria.negocio.casouso.ejemplar.impl.ConsultarTodosEjemplaresCasoUsoImpl;
import com.libreria.negocio.fachada.ejemplar.ConsultarTodosEjemplaresFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosEjemplaresFachadaImpl implements ConsultarTodosEjemplaresFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosEjemplaresCasoUso casoUso;

    public ConsultarTodosEjemplaresFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosEjemplaresCasoUsoImpl(daoFactory);
    }

    @Override
    public List<EjemplarDTO> ejecutar(final EjemplarDTO filtro) {
        try {
            final EjemplarDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new EjemplarDTO.Builder().build());
            final EjemplarEntidad filtroEntidad = new EjemplarEntidad.Builder()
                    .libro(filtroEfectivo.getLibro() != null && filtroEfectivo.getLibro().getId() != null
                            ? new LibroEntidad.Builder()
                                    .id(filtroEfectivo.getLibro().getId())
                                    .build()
                            : null)
                    .build();

            final List<EjemplarEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<EjemplarDTO> resultado = new ArrayList<>();
            for (final EjemplarEntidad entidad : entidades) {
                resultado.add(new EjemplarDTO.Builder()
                        .id(entidad.getId())
                        .libro(new LibroDTO.Builder()
                                .id(entidad.getLibro().getId())
                                .build())
                        .signatura(new SignaturaDTO.Builder()
                                .id(entidad.getSignatura().getId())
                                .build())
                        .build());
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los ejemplares.", "Error técnico inesperado en ConsultarTodosEjemplaresFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
