package com.libreria.negocio.casouso.ejemplar.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.ejemplar.ActualizarEjemplarCasoUso;
import com.libreria.negocio.dominio.EjemplarDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarEjemplarCasoUsoImpl implements ActualizarEjemplarCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarEjemplarCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final EjemplarDominio datos) {
        // P4 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P5 — Validar que el ejemplar exista en el sistema
        validarExistenciaEjemplar(datos.getId());
        // P2 — Validar que el libro exista en el sistema
        validarExistenciaLibro(datos.getLibro().getId());
        // Validar que la signatura exista en el sistema
        validarExistenciaSignatura(datos.getSignatura().getId());
        
        daoFactory.getEjemplarDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P4 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final EjemplarDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del ejemplar son obligatorios.", "Se recibió un objeto EjemplarDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del ejemplar es obligatorio.", "El campo id llegó nulo en EjemplarDominio.");
        }
        if (UtilObjeto.esNulo(datos.getLibro()) || UtilUUID.esNulo(datos.getLibro().getId())) {
            throw GestorLibreriaExcepcion.crear("El libro del ejemplar es obligatorio.", "El campo libroId llegó nulo en EjemplarDominio.");
        }
        if (UtilObjeto.esNulo(datos.getSignatura()) || UtilUUID.esNulo(datos.getSignatura().getId())) {
            throw GestorLibreriaExcepcion.crear("La signatura del ejemplar es obligatoria.", "El campo signaturaId llegó nulo en EjemplarDominio.");
        }
    }

    // P5 — Validar que el ejemplar exista en el sistema
    private void validarExistenciaEjemplar(final UUID id) {
        final EjemplarEntidad entidad = daoFactory.getEjemplarDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El ejemplar indicado no existe en el sistema.", "No se encontró ejemplar con id: " + id);
        }
    }

    // P2 — Validar que el libro exista en el sistema
    private void validarExistenciaLibro(final UUID libroId) {
        final LibroEntidad libro = daoFactory.getLibroDAO().consultarPorId(libroId);
        if (UtilObjeto.esNulo(libro) || UtilObjeto.esNulo(libro.getId())) {
            throw GestorLibreriaExcepcion.crear("El libro indicado no existe en el sistema.", "No se encontró libro con id: " + libroId);
        }
    }

    // Validar que la signatura exista en el sistema
    private void validarExistenciaSignatura(final UUID signaturaId) {
        final SignaturaEntidad signatura = daoFactory.getSignaturaDAO().consultarPorId(signaturaId);
        if (UtilObjeto.esNulo(signatura) || UtilObjeto.esNulo(signatura.getId())) {
            throw GestorLibreriaExcepcion.crear("La signatura indicada no existe en el sistema.", "No se encontró signatura con id: " + signaturaId);
        }
    }

    private EjemplarEntidad construirEntidad(final EjemplarDominio datos) {
        return new EjemplarEntidad.Builder()
                .id(datos.getId())
                .libro(new LibroEntidad.Builder()
                        .id(datos.getLibro().getId())
                        .build())
                .signatura(new SignaturaEntidad.Builder()
                        .id(datos.getSignatura().getId())
                        .build())
                .build();
    }

}
