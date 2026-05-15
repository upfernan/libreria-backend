package com.libreria.negocio.fachada.signatura.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.SignaturaDTO;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.ConsultarSignaturaPorIdCasoUso;
import com.libreria.negocio.casouso.signatura.impl.ConsultarSignaturaPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.signatura.ConsultarSignaturaPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarSignaturaPorIdFachadaImpl implements ConsultarSignaturaPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarSignaturaPorIdCasoUso casoUso;

    public ConsultarSignaturaPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarSignaturaPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public SignaturaDTO ejecutar(final UUID id) {
        try {
            final SignaturaEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new SignaturaEntidad.Builder().build());
            return new SignaturaDTO.Builder()
                    .id(entidad.getId())
                    .pasillo(entidad.getPasillo())
                    .estante(entidad.getEstante())
                    .posicion(entidad.getPosicion())
                    .build();

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar la signatura.", "Error técnico inesperado en ConsultarSignaturaPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
