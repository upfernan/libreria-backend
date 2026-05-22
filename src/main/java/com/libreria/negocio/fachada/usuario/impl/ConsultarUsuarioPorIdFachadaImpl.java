package com.libreria.negocio.fachada.usuario.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.UsuarioDTO;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.assembler.dto.impl.UsuarioDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.UsuarioEntidadAssembler;
import com.libreria.negocio.casouso.usuario.ConsultarUsuarioPorIdCasoUso;
import com.libreria.negocio.casouso.usuario.impl.ConsultarUsuarioPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.usuario.ConsultarUsuarioPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarUsuarioPorIdFachadaImpl implements ConsultarUsuarioPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarUsuarioPorIdCasoUso casoUso;

    public ConsultarUsuarioPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarUsuarioPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public UsuarioDTO ejecutar(final UUID id) {
        try {
            final UsuarioEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new UsuarioEntidad.Builder().build());
            return UsuarioDTOAssembler.getInstance().ensamblarDTO(
                    UsuarioEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el usuario.", "Error técnico inesperado en ConsultarUsuarioPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
