package com.libreria.negocio.casouso.tipolibro.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.tipolibro.RetirarTipoLibroCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarTipoLibroCasoUsoImpl implements RetirarTipoLibroCasoUso {

    private final DAOFactory daoFactory;

    public RetirarTipoLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P4 — Validar que el tipo de libro exista en el sistema
        validarExistencia(id);
        // P5 — Validar que el tipo de libro no esté en uso
        validarNoEnUso(id);
        // P1 — Eliminar el tipo de libro del sistema
        daoFactory.getTipoLibroDAO().eliminar(id);
    }

    // P4 — Validar que el tipo de libro exista en el sistema
    private void validarExistencia(final UUID id) {
        final TipoLibroEntidad entidad = daoFactory.getTipoLibroDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El tipo de libro indicado no existe en el sistema.", "No se encontró TipoLibro con id: " + id);
        }
    }

    // P5 — Validar que el tipo de libro no esté siendo utilizado por algún libro
    private void validarNoEnUso(final UUID id) {
        final LibroEntidad filtroLibro = new LibroEntidad.Builder()
                .tipoLibro(new TipoLibroEntidad.Builder().id(id).build()).build();
        final List<LibroEntidad> libros = daoFactory.getLibroDAO().consultarPorFiltro(filtroLibro);
        if (!UtilObjeto.esNulo(libros) && !libros.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El tipo de libro está en uso y no puede eliminarse.", "tipoLibroId: " + id);
        }
    }
}
