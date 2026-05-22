package com.libreria.negocio.casouso.ejemplar.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.ejemplar.RetirarEjemplarCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEjemplarCasoUsoImpl implements RetirarEjemplarCasoUso {

    private final DAOFactory daoFactory;

    public RetirarEjemplarCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador del ejemplar es obligatorio.", "Se recibió un UUID nulo para retirar ejemplar.");
        }
        // P5 — Validar que el ejemplar exista en el sistema
        final EjemplarEntidad ejemplar = validarExistencia(id);
        // P6 — Validar que el ejemplar no tenga préstamos (activos ni históricos)
        validarNoEnUso(id);
        
        daoFactory.getEjemplarDAO().eliminar(id);
        // p8 Decrementar disponibles del libro si el ejemplar era de tipo físico
        decrementarDisponiblesLibro(ejemplar);
    }

    // P5 — Validar que el ejemplar exista en el sistema
    private EjemplarEntidad validarExistencia(final UUID id) {
        final EjemplarEntidad entidad = daoFactory.getEjemplarDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El ejemplar indicado no existe en el sistema.", "No se encontró Ejemplar con id: " + id);
        }
        return entidad;
    }

    // p8 Decrementar disponibles del libro asociado (solo para libros físicos, nunca por debajo de cero)
    private void decrementarDisponiblesLibro(final EjemplarEntidad ejemplar) {
        final LibroEntidad libro = daoFactory.getLibroDAO().consultarPorId(ejemplar.getLibro().getId());
        if (UtilObjeto.esNulo(libro) || UtilObjeto.esNulo(libro.getId())) {
            return;
        }
        if (!"físico".equalsIgnoreCase(libro.getTipoLibro().getNombre())) {
            return;
        }
        final int nuevosDisponibles = Math.max(0, libro.getDisponibles() - 1);
        daoFactory.getLibroDAO().actualizar(libro.getId(), new LibroEntidad.Builder()
                .id(libro.getId())
                .titulo(libro.getTitulo())
                .tipoLibro(libro.getTipoLibro())
                .categoria(libro.getCategoria())
                .editorial(libro.getEditorial())
                .disponibles(nuevosDisponibles)
                .build());
    }

    // P6 — Validar que el ejemplar no tenga préstamos (activos ni históricos)
    private void validarNoEnUso(final UUID id) {
        final PrestamoEntidad filtro = new PrestamoEntidad.Builder()
                .ejemplar(new EjemplarEntidad.Builder().id(id).build())
                .build();
        final List<PrestamoEntidad> prestamos = daoFactory.getPrestamoDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(prestamos) && !prestamos.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El ejemplar tiene préstamos registrados y no puede eliminarse.", "ejemplarId: " + id);
        }
    }

}
