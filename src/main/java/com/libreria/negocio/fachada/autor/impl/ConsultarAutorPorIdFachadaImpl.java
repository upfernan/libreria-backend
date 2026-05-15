package com.libreria.negocio.fachada.autor.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.casouso.autor.ConsultarAutorPorIdCasoUso;
import com.libreria.negocio.casouso.autor.impl.ConsultarAutorPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.autor.ConsultarAutorPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarAutorPorIdFachadaImpl implements ConsultarAutorPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarAutorPorIdCasoUso casoUso;

    public ConsultarAutorPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarAutorPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public AutorDTO ejecutar(final UUID id) {
        try {
            final AutorEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new AutorEntidad.Builder().build());
            return new AutorDTO.Builder()
                    .id(entidad.getId())
                    .primerNombre(entidad.getPrimerNombre())
                    .segundoNombre(entidad.getSegundoNombre())
                    .primerApellido(entidad.getPrimerApellido())
                    .segundoApellido(entidad.getSegundoApellido())
                    .build();

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el autor.", "Error técnico inesperado en ConsultarAutorPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
