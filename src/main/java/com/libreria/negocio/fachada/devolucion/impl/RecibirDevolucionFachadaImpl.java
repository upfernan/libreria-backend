package com.libreria.negocio.fachada.devolucion.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.DevolucionDTO;
import com.libreria.dto.PrestamoDTO;
import com.libreria.negocio.casouso.devolucion.RecibirDevolucionCasoUso;
import com.libreria.negocio.casouso.devolucion.impl.RecibirDevolucionCasoUsoImpl;
import com.libreria.negocio.dominio.DevolucionDominio;
import com.libreria.negocio.dominio.PrestamoDominio;
import com.libreria.negocio.fachada.devolucion.RecibirDevolucionFachada;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RecibirDevolucionFachadaImpl implements RecibirDevolucionFachada {

    private final DAOFactory daoFactory;
    private final RecibirDevolucionCasoUso casoUso;

    public RecibirDevolucionFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new RecibirDevolucionCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final DevolucionDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            final DevolucionDTO datosEfectivos = UtilObjeto.obtenerValorDefecto(datos, new DevolucionDTO.Builder().build());
            final PrestamoDTO prestamoEfectivo = UtilObjeto.obtenerValorDefecto(datosEfectivos.getPrestamo(), new PrestamoDTO.Builder().build());

            final DevolucionDominio dominio = new DevolucionDominio.Builder()
                    .prestamo(new PrestamoDominio.Builder()
                            .id(prestamoEfectivo.getId())
                            .build())
                    .build();

            casoUso.ejecutar(dominio);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al registrar la devolución.", "Error técnico inesperado en RecibirDevolucionFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
