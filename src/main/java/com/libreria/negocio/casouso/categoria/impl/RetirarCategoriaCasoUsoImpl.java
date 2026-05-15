package com.libreria.negocio.casouso.categoria.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.categoria.RetirarCategoriaCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarCategoriaCasoUsoImpl implements RetirarCategoriaCasoUso {

    private final DAOFactory daoFactory;

    public RetirarCategoriaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P4 — Validar que la categoría exista en el sistema
        validarExistencia(id);
        // P5 — Validar que la categoría no esté en uso
        validarNoEnUso(id);
        // P1 — Eliminar la categoría del sistema
        daoFactory.getCategoriaDAO().eliminar(id);
    }

    // P4 — Validar que la categoría exista en el sistema
    private void validarExistencia(final UUID id) {
        final CategoriaEntidad entidad = daoFactory.getCategoriaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La categoría indicada no existe en el sistema.", "No se encontró categoría con id: " + id);
        }
    }

    // P5 — Validar que la categoría no esté siendo utilizada por algún libro
    private void validarNoEnUso(final UUID id) {
        final LibroEntidad filtroLibro = new LibroEntidad.Builder()
                .categoria(new CategoriaEntidad.Builder().id(id).build()).build();
        final List<LibroEntidad> libros = daoFactory.getLibroDAO().consultarPorFiltro(filtroLibro);
        if (!UtilObjeto.esNulo(libros) && !libros.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("La categoría está en uso y no puede eliminarse.", "categoriaId: " + id);
        }
    }
}
