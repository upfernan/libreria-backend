package com.libreria.negocio.fachada.pago.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.MultaDTO;
import com.libreria.dto.PagoDTO;
import com.libreria.negocio.casouso.pago.RecibirPagoMultaCasoUso;
import com.libreria.negocio.casouso.pago.impl.RecibirPagoMultaCasoUsoImpl;
import com.libreria.negocio.dominio.MultaDominio;
import com.libreria.negocio.dominio.PagoDominio;
import com.libreria.negocio.fachada.pago.RecibirPagoMultaFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RecibirPagoMultaFachadaImpl implements RecibirPagoMultaFachada {

    private final DAOFactory daoFactory;
    private final RecibirPagoMultaCasoUso casoUso;

    public RecibirPagoMultaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RecibirPagoMultaCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final PagoDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            final PagoDTO datosEfectivos = UtilObjeto.obtenerValorDefecto(datos, new PagoDTO.Builder().build());
            final MultaDTO multaEfectiva = UtilObjeto.obtenerValorDefecto(datosEfectivos.getMulta(), new MultaDTO.Builder().build());

            final PagoDominio dominio = new PagoDominio.Builder()
                    .multa(new MultaDominio.Builder()
                            .id(multaEfectiva.getId())
                            .build())
                    .build();

            casoUso.ejecutar(dominio);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar el pago de la multa.", "Error técnico inesperado en RecibirPagoMultaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
