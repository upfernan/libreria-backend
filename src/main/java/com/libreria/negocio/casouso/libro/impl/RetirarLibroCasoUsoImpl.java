package com.libreria.negocio.casouso.libro.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.libro.RetirarLibroCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarLibroCasoUsoImpl implements RetirarLibroCasoUso {

    private final DAOFactory daoFactory;

    public RetirarLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador del libro es obligatorio.", "Se recibió un UUID nulo para retirar libro.");
        }
        // P3 — Validar que el libro exista en el sistema
        validarExistencia(id);
        // P4 — Validar que el libro no tenga ejemplares registrados
        validarNoEnUso(id);
        // P1 — Eliminar el libro del sistema
        daoFactory.getLibroDAO().eliminar(id);
    }

    // P3 — Validar que el libro exista en el sistema
    private void validarExistencia(final UUID id) {
        final LibroEntidad entidad = daoFactory.getLibroDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El libro indicado no existe en el sistema.", "No se encontró Libro con id: " + id);
        }
    }

    // P4 — Validar que el libro no tenga ejemplares asociados
    private void validarNoEnUso(final UUID id) {
        final EjemplarEntidad filtro = new EjemplarEntidad.Builder()
                .libro(new LibroEntidad.Builder().id(id).build())
                .build();
        final List<EjemplarEntidad> ejemplares = daoFactory.getEjemplarDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(ejemplares) && !ejemplares.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El libro tiene ejemplares registrados y no puede eliminarse.", "libroId: " + id);
        }
    }

}
