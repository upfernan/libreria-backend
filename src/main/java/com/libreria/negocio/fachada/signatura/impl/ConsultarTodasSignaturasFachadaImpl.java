package com.libreria.negocio.fachada.signatura.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.SignaturaDTO;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.assembler.dto.impl.SignaturaDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.SignaturaEntidadAssembler;
import com.libreria.negocio.casouso.signatura.ConsultarTodasSignaturasCasoUso;
import com.libreria.negocio.casouso.signatura.impl.ConsultarTodasSignaturasCasoUsoImpl;
import com.libreria.negocio.fachada.signatura.ConsultarTodasSignaturasFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodasSignaturasFachadaImpl implements ConsultarTodasSignaturasFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodasSignaturasCasoUso casoUso;

    public ConsultarTodasSignaturasFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodasSignaturasCasoUsoImpl(daoFactory);
    }

    @Override
    public List<SignaturaDTO> ejecutar(final SignaturaDTO filtro) {
        try {
            final SignaturaDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new SignaturaDTO.Builder().build());
            final SignaturaEntidad filtroEntidad = SignaturaEntidadAssembler.getInstance().ensamblarEntidad(
                    SignaturaDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<SignaturaEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<SignaturaDTO> resultado = new ArrayList<>();
            for (final SignaturaEntidad entidad : entidades) {
                resultado.add(SignaturaDTOAssembler.getInstance().ensamblarDTO(
                        SignaturaEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las signaturas.", "Error técnico inesperado en ConsultarTodasSignaturasFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
