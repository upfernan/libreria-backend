package com.libreria.negocio.fachada.prestamo.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.PrestamoDTO;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.assembler.dto.impl.PrestamoDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.PrestamoEntidadAssembler;
import com.libreria.negocio.casouso.prestamo.ConsultarPrestamoPorIdCasoUso;
import com.libreria.negocio.casouso.prestamo.impl.ConsultarPrestamoPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.prestamo.ConsultarPrestamoPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarPrestamoPorIdFachadaImpl implements ConsultarPrestamoPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarPrestamoPorIdCasoUso casoUso;

    public ConsultarPrestamoPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarPrestamoPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public PrestamoDTO ejecutar(final UUID id) {
        try {
            final PrestamoEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new PrestamoEntidad.Builder().build());
            return PrestamoDTOAssembler.getInstance().ensamblarDTO(
                    PrestamoEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar el préstamo.", "Error técnico inesperado en ConsultarPrestamoPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
