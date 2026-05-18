package com.libreria.negocio.fachada.ejemplar.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EjemplarDTO;
import com.libreria.dto.LibroDTO;
import com.libreria.dto.SignaturaDTO;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.negocio.casouso.ejemplar.ConsultarEjemplarPorIdCasoUso;
import com.libreria.negocio.casouso.ejemplar.impl.ConsultarEjemplarPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.ejemplar.ConsultarEjemplarPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarEjemplarPorIdFachadaImpl implements ConsultarEjemplarPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarEjemplarPorIdCasoUso casoUso;

    public ConsultarEjemplarPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarEjemplarPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public EjemplarDTO ejecutar(final UUID id) {
        try {
            final EjemplarEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new EjemplarEntidad.Builder().build());
            return new EjemplarDTO.Builder()
                    .id(entidad.getId())
                    .libro(new LibroDTO.Builder()
                            .id(entidad.getLibro().getId())
                            .build())
                    .signatura(new SignaturaDTO.Builder()
                            .id(entidad.getSignatura().getId())
                            .build())
                    .build();

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el ejemplar.", "Error técnico inesperado en ConsultarEjemplarPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
