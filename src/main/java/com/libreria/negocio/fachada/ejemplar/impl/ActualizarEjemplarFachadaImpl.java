package com.libreria.negocio.fachada.ejemplar.impl;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.dto.EjemplarDTO;
import com.libreria.negocio.casouso.ejemplar.ActualizarEjemplarCasoUso;
import com.libreria.negocio.casouso.ejemplar.impl.ActualizarEjemplarCasoUsoImpl;
import com.libreria.negocio.dominio.EjemplarDominio;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.negocio.dominio.SignaturaDominio;
import com.libreria.negocio.fachada.ejemplar.ActualizarEjemplarFachada;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEjemplarFachadaImpl implements ActualizarEjemplarFachada {

    private final DAOFactory daoFactory;
    private final ActualizarEjemplarCasoUso casoUso;

    public ActualizarEjemplarFachadaImpl() {
        daoFactory = DAOFactory.getFactory();
        casoUso = new ActualizarEjemplarCasoUsoImpl(daoFactory);
    }

    @Override
    public void ejecutar(final EjemplarDTO datos) {
        try {
            daoFactory.iniciarTransaccion();

            final EjemplarDominio dominio = new EjemplarDominio.Builder()
                    .id(datos.getId())
                    .libro(new LibroDominio.Builder()
                            .id(datos.getLibro().getId())
                            .build())
                    .signatura(new SignaturaDominio.Builder()
                            .id(datos.getSignatura().getId())
                            .build())
                    .build();

            casoUso.ejecutar(dominio);

            daoFactory.confirmarTransaccion();

        } catch (GestorLibreriaExcepcion excepcion) {
            daoFactory.cancelarTransaccion();
            throw excepcion;

        } catch (Exception excepcion) {
            daoFactory.cancelarTransaccion();
            throw GestorLibreriaExcepcion.crear(excepcion, "Ocurrió un error inesperado al actualizar el ejemplar.", "Error técnico inesperado en ActualizarEjemplarFachadaImpl: " + excepcion.getMessage());

        } finally {
            daoFactory.cerrarConexion();
        }
    }
    
    public static void main(String[] args) {
		final ActualizarEjemplarFachada fachada = new ActualizarEjemplarFachadaImpl();
		final EjemplarDTO datos = new EjemplarDTO.Builder()
				.id(java.util.UUID.fromString("a658c3d2-b4ec-48c2-a735-96912117b4ce"))
				.libro(new com.libreria.dto.LibroDTO.Builder()
						.id(java.util.UUID.fromString("539dec14-ad5b-4dc0-832a-5753bda78a80"))
						.build())
				.signatura(new com.libreria.dto.SignaturaDTO.Builder()
						.id(java.util.UUID.fromString("bbb2b58a-1ccf-4d58-bd60-cdb22aad4c0d"))
						.build())
				.build();
		fachada.ejecutar(datos);
		System.out.println("Ejemplar actualizado exitosamente.");

}
}
