package com.libreria.negocio.fachada.pago.impl;

import java.util.ArrayList;
import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.PagoDTO;
import com.libreria.entidad.PagoEntidad;
import com.libreria.negocio.assembler.dto.impl.PagoDTOAssembler;
import com.libreria.negocio.assembler.entidad.impl.PagoEntidadAssembler;
import com.libreria.negocio.casouso.pago.ConsultarTodosPagosCasoUso;
import com.libreria.negocio.casouso.pago.impl.ConsultarTodosPagosCasoUsoImpl;
import com.libreria.negocio.fachada.pago.ConsultarTodosPagosFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarTodosPagosFachadaImpl implements ConsultarTodosPagosFachada {

    private final DAOFactory daoFactory;
    private final ConsultarTodosPagosCasoUso casoUso;

    public ConsultarTodosPagosFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ConsultarTodosPagosCasoUsoImpl(daoFactory);
    }

    @Override
    public List<PagoDTO> ejecutar(final PagoDTO filtro) {
        try {
            final PagoDTO filtroEfectivo = UtilObjeto.obtenerValorDefecto(filtro, new PagoDTO.Builder().build());
            final PagoEntidad filtroEntidad = PagoEntidadAssembler.getInstance().ensamblarEntidad(
                    PagoDTOAssembler.getInstance().ensamblarDominio(filtroEfectivo));

            final List<PagoEntidad> entidades = casoUso.ejecutar(filtroEntidad);
            final List<PagoDTO> resultado = new ArrayList<>();
            for (final PagoEntidad entidad : entidades) {
                resultado.add(PagoDTOAssembler.getInstance().ensamblarDTO(
                        PagoEntidadAssembler.getInstance().ensamblarDominio(entidad)));
            }
            return resultado;

        } catch (GestorLibreriaExcepcion excepcion) {
            throw excepcion;

        } catch (Exception excepcion) {
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al consultar los pagos.", "Error técnico inesperado en ConsultarTodosPagosFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
