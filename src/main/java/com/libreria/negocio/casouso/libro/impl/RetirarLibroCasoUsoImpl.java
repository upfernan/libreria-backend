package com.libreria.negocio.casouso.libro.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.EstadoReservaEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.ReservaEntidad;
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
        
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador del libro es obligatorio.", "Se recibió un UUID nulo para retirar libro.");
        }
        // P6 — Validar que el libro exista en el sistema
        validarExistencia(id);
        // P7 — Validar que el libro no tenga ejemplares registrados
        validarNoEnUso(id);
        // P8 — Validar que el libro no tenga reservas activas
        validarSinReservasActivas(id);
    
        daoFactory.getLibroDAO().eliminar(id);
    }

    // P6 — Validar que el libro exista en el sistema
    private void validarExistencia(final UUID id) {
        final LibroEntidad entidad = daoFactory.getLibroDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El libro indicado no existe en el sistema.", "No se encontró Libro con id: " + id);
        }
    }

    // P7 — Validar que el libro no tenga ejemplares asociados
    private void validarNoEnUso(final UUID id) {
        final EjemplarEntidad filtro = new EjemplarEntidad.Builder()
                .libro(new LibroEntidad.Builder().id(id).build())
                .build();
        final List<EjemplarEntidad> ejemplares = daoFactory.getEjemplarDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(ejemplares) && !ejemplares.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El libro tiene ejemplares registrados y no puede eliminarse.", "libroId: " + id);
        }
    }

    // P8 — Validar que el libro no tenga reservas activas
    private void validarSinReservasActivas(final UUID id) {
        final ReservaEntidad filtro = new ReservaEntidad.Builder()
                .libro(new LibroEntidad.Builder().id(id).build())
                .estadoReserva(new EstadoReservaEntidad.Builder().nombre("pendiente").build())
                .build();
        final List<ReservaEntidad> reservasActivas = daoFactory.getReservaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(reservasActivas) && !reservasActivas.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El libro tiene reservas activas y no puede eliminarse.", "libroId: " + id);
        }
    }

}
