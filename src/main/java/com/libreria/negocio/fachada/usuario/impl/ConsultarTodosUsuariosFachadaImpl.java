package com.libreria.negocio.fachada.usuario.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.TipoIdentificacionDTO;
import com.libreria.dto.UsuarioDTO;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.usuario.ConsultarTodosUsuariosCasoUso;
import com.libreria.negocio.casouso.usuario.impl.ConsultarTodosUsuariosCasoUsoImpl;
import com.libreria.negocio.fachada.usuario.ConsultarTodosUsuariosFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosUsuariosFachadaImpl implements ConsultarTodosUsuariosFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosUsuariosCasoUso casoUso;

    public ConsultarTodosUsuariosFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosUsuariosCasoUsoImpl(daoFactory);
    }

    @Override
    public List<UsuarioDTO> ejecutar(final UsuarioDTO filtro) {
        try {
            final UsuarioDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new UsuarioDTO.Builder().build());
            final UsuarioEntidad filtroEntidad = new UsuarioEntidad.Builder()
                    .tipoIdentificacion(filtroEfectivo.getTipoIdentificacion() != null
                            ? new com.libreria.entidad.TipoIdentificacionEntidad.Builder()
                                    .id(filtroEfectivo.getTipoIdentificacion().getId())
                                    .build()
                            : null)
                    .build();

            final List<UsuarioEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<UsuarioDTO> resultado = new ArrayList<>();
            for (final UsuarioEntidad entidad : entidades) {
                resultado.add(new UsuarioDTO.Builder()
                        .id(entidad.getId())
                        .tipoIdentificacion(new TipoIdentificacionDTO.Builder()
                                .id(entidad.getTipoIdentificacion().getId())
                                .build())
                        .numeroIdentificacion(entidad.getNumeroIdentificacion())
                        .primerNombre(entidad.getPrimerNombre())
                        .segundoNombre(entidad.getSegundoNombre())
                        .primerApellido(entidad.getPrimerApellido())
                        .segundoApellido(entidad.getSegundoApellido())
                        .correoElectronico(entidad.getCorreoElectronico())
                        .build());
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los usuarios.", "Error técnico inesperado en ConsultarTodosUsuariosFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
