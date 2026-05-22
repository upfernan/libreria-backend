package com.libreria.negocio.fachada.reserva.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.ReservaDTO;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.assembler.dto.impl.ReservaDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.ReservaEntidadAssembler;
import com.libreria.negocio.casouso.reserva.ConsultarReservaPorIdCasoUso;
import com.libreria.negocio.casouso.reserva.impl.ConsultarReservaPorIdCasoUsoImpl;
import com.libreria.negocio.fachada.reserva.ConsultarReservaPorIdFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarReservaPorIdFachadaImpl implements ConsultarReservaPorIdFachada {

    private final DAOFactory daoFactory;
    private final ConsultarReservaPorIdCasoUso casoUso;

    public ConsultarReservaPorIdFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarReservaPorIdCasoUsoImpl(daoFactory);
    }

    @Override
    public ReservaDTO ejecutar(final UUID id) {
        try {
            final ReservaEntidad entidad = UtilObjeto.obtenerValorDefecto(casoUso.ejecutar(id), new ReservaEntidad.Builder().build());
            return ReservaDTOAssembler.getInstance().ensamblarDTO(
                    ReservaEntidadAssembler.getInstance().ensamblarDominio(entidad));

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar la reserva.", "Error técnico inesperado en ConsultarReservaPorIdFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
