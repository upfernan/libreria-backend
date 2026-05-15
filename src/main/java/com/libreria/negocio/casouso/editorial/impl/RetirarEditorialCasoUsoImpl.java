package com.libreria.negocio.casouso.editorial.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.editorial.RetirarEditorialCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarEditorialCasoUsoImpl implements RetirarEditorialCasoUso {

    private final DAOFactory daoFactory;

    public RetirarEditorialCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P4 — Validar que la editorial exista en el sistema
        validarExistencia(id);
        // P5 — Validar que la editorial no esté en uso
        validarNoEnUso(id);
        // P1 — Eliminar la editorial del sistema
        daoFactory.getEditorialDAO().eliminar(id);
    }

    // P4 — Validar que la editorial exista en el sistema
    private void validarExistencia(final UUID id) {
        final EditorialEntidad entidad = daoFactory.getEditorialDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La editorial indicada no existe en el sistema.", "No se encontró editorial con id: " + id);
        }
    }

    // P5 — Validar que la editorial no esté siendo utilizada por algún libro
    private void validarNoEnUso(final UUID id) {
        final LibroEntidad filtroLibro = new LibroEntidad.Builder()
                .editorial(new EditorialEntidad.Builder().id(id).build()).build();
        final List<LibroEntidad> libros = daoFactory.getLibroDAO().consultarPorFiltro(filtroLibro);
        if (!UtilObjeto.esNulo(libros) && !libros.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("La editorial está en uso y no puede eliminarse.", "editorialId: " + id);
        }
    }
}
