package com.libreria.negocio.fachada.reserva.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.ReservaDTO;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.assembler.dto.impl.ReservaDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.ReservaEntidadAssembler;
import com.libreria.negocio.casouso.reserva.ConsultarTodasReservasCasoUso;
import com.libreria.negocio.casouso.reserva.impl.ConsultarTodasReservasCasoUsoImpl;
import com.libreria.negocio.fachada.reserva.ConsultarTodasReservasFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodasReservasFachadaImpl implements ConsultarTodasReservasFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodasReservasCasoUso casoUso;

    public ConsultarTodasReservasFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodasReservasCasoUsoImpl(daoFactory);
    }

    @Override
    public List<ReservaDTO> ejecutar(final ReservaDTO filtro) {
        try {
            final ReservaDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new ReservaDTO.Builder().build());
            final ReservaEntidad filtroEntidad = ReservaEntidadAssembler.getInstance().ensamblarEntidad(
                    ReservaDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<ReservaEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<ReservaDTO> resultado = new ArrayList<>();
            for (final ReservaEntidad entidad : entidades) {
                resultado.add(ReservaDTOAssembler.getInstance().ensamblarDTO(
                        ReservaEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar las reservas.", "Error técnico inesperado en ConsultarTodasReservasFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }

}
