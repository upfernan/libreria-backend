package com.libreria.negocio.fachada.multa.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.DevolucionDTO;
import com.libreria.dto.MultaDTO;
import com.libreria.negocio.casouso.multa.CobrarMultaCasoUso;
import com.libreria.negocio.casouso.multa.impl.CobrarMultaCasoUsoImpl;
import com.libreria.negocio.dominio.DevolucionDominio;
import com.libreria.negocio.dominio.MultaDominio;
import com.libreria.negocio.fachada.multa.CobrarMultaFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class CobrarMultaFachadaImpl implements CobrarMultaFachada {

    private final DAOFactory daoFactory;
    private final CobrarMultaCasoUso casoUso;

    public CobrarMultaFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new CobrarMultaCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final MultaDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            final MultaDTO datosEfectivos = UtilObjeto.obtenerValorDefecto(datos, new MultaDTO.Builder().build());
            final DevolucionDTO devolucionEfectiva = UtilObjeto.obtenerValorDefecto(datosEfectivos.getDevolucion(), new DevolucionDTO.Builder().build());

            final MultaDominio dominio = new MultaDominio.Builder()
                    .devolucion(new DevolucionDominio.Builder()
                            .id(devolucionEfectiva.getId())
                            .build())
                    .build();

            casoUso.ejecutar(dominio);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al cobrar la multa.", "Error técnico inesperado en CobrarMultaFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
