package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.autorlibro.QuitarAutorLibroCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class QuitarAutorLibroCasoUsoImpl implements QuitarAutorLibroCasoUso {

    private final DAOFactory daoFactory;

    public QuitarAutorLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P6 — Validar que la relación autor-libro exista en el sistema
        validarExistencia(id);
       
        daoFactory.getAutorLibroDAO().eliminar(id);
    }

    // P6 — Validar que la relación autor-libro exista en el sistema
    private void validarExistencia(final UUID id) {
        final AutorLibroEntidad entidad = daoFactory.getAutorLibroDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La relación autor-libro indicada no existe en el sistema.", "No se encontró AutorLibro con id: " + id);
        }
    }
}
