package com.libreria.negocio.casouso.autor.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorEntidad;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.autor.RetirarAutorCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarAutorCasoUsoImpl implements RetirarAutorCasoUso {

    private final DAOFactory daoFactory;

    public RetirarAutorCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P3 — Validar que el autor exista en el sistema
        validarExistencia(id);
        // P4 — Validar que el autor no esté en uso
        validarNoEnUso(id);
        // P1 — Eliminar el autor del sistema
        daoFactory.getAutorDAO().eliminar(id);
    }

    // P3 — Validar que el autor exista en el sistema
    private void validarExistencia(final UUID id) {
        final AutorEntidad entidad = daoFactory.getAutorDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El autor indicado no existe en el sistema.", "No se encontró Autor con id: " + id);
        }
    }

    // P4 — Validar que el autor no esté siendo utilizado por ningún libro
    private void validarNoEnUso(final UUID id) {
        final AutorLibroEntidad filtro = new AutorLibroEntidad.Builder()
                .autor(new AutorEntidad.Builder().id(id).build())
                .build();
        final List<AutorLibroEntidad> autorLibros = daoFactory.getAutorLibroDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(autorLibros) && !autorLibros.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("El autor está en uso y no puede eliminarse.", "autorId: " + id);
        }
    }
}
