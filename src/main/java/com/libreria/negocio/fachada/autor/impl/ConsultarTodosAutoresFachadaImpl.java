package com.libreria.negocio.fachada.autor.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.AutorDTO;
import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.assembler.dto.impl.AutorDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.AutorEntidadAssembler;
import com.libreria.negocio.casouso.autor.ConsultarTodosAutoresCasoUso;
import com.libreria.negocio.casouso.autor.impl.ConsultarTodosAutoresCasoUsoImpl;
import com.libreria.negocio.dominio.AutorDominio;
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
            final AutorEntidad filtroEntidad = AutorEntidadAssembler.getInstance().ensamblarEntidad(
                    AutorDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<AutorEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<AutorDTO> resultado = new ArrayList<>();
            for (final AutorEntidad entidad : entidades) {
                final AutorDominio dominio = AutorEntidadAssembler.getInstance().ensamblarDominio(entidad);
                resultado.add(AutorDTOAssembler.getInstance().ensamblarDTO(dominio));
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
