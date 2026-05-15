package com.libreria.negocio.fachada.autor.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.casouso.autor.ConsultarTodosAutoresCasoUso;
import com.libreria.negocio.casouso.autor.impl.ConsultarTodosAutoresCasoUsoImpl;
import com.libreria.negocio.fachada.autor.ConsultarTodosAutoresFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosAutoresFachadaImpl implements ConsultarTodosAutoresFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosAutoresCasoUso casoUso;

    public ConsultarTodosAutoresFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosAutoresCasoUsoImpl(daoFactory);
    }

    @Override
    public List<AutorDTO> ejecutar(final AutorDTO filtro) {
        try {
            final AutorDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new AutorDTO.Builder().build());
            final AutorEntidad filtroEntidad = new AutorEntidad.Builder()
                    .primerNombre(filtroEfectivo.getPrimerNombre())
                    .segundoNombre(filtroEfectivo.getSegundoNombre())
                    .primerApellido(filtroEfectivo.getPrimerApellido())
                    .segundoApellido(filtroEfectivo.getSegundoApellido())
                    .build();

            final List<AutorEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<AutorDTO> resultado = new ArrayList<>();
            for (final AutorEntidad entidad : entidades) {
                resultado.add(new AutorDTO.Builder()
                        .id(entidad.getId())
                        .primerNombre(entidad.getPrimerNombre())
                        .segundoNombre(entidad.getSegundoNombre())
                        .primerApellido(entidad.getPrimerApellido())
                        .segundoApellido(entidad.getSegundoApellido())
                        .build());
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los autores.", "Error técnico inesperado en ConsultarTodosAutoresFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
