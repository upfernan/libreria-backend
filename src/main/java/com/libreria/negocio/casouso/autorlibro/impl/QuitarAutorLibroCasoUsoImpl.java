package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.entidad.LibroEntidad;
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
        final AutorLibroEntidad autorLibro = validarExistencia(id);
        // P7 — Validar que el libro quede con al menos un autor asociado
        validarLibroConAlMenosUnAutor(autorLibro);
        // P1 — Eliminar la relación autor-libro del sistema
        daoFactory.getAutorLibroDAO().eliminar(id);
    }

    // P6 — Validar que la relación autor-libro exista en el sistema
    private AutorLibroEntidad validarExistencia(final UUID id) {
        final AutorLibroEntidad entidad = daoFactory.getAutorLibroDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La relación autor-libro indicada no existe en el sistema.", "No se encontró AutorLibro con id: " + id);
        }
        return entidad;
    }

    // P7 — El libro debe quedar con al menos un autor asociado tras la eliminación
    private void validarLibroConAlMenosUnAutor(final AutorLibroEntidad autorLibro) {
        final UUID libroId = autorLibro.getLibro().getId();
        final AutorLibroEntidad filtro = new AutorLibroEntidad.Builder()
                .libro(new LibroEntidad.Builder().id(libroId).build())
                .build();
        final List<AutorLibroEntidad> autoresDelLibro = daoFactory.getAutorLibroDAO().consultarPorFiltro(filtro);
        if (UtilObjeto.esNulo(autoresDelLibro) || autoresDelLibro.size() <= 1) {
            throw GestorLibreriaExcepcion.crear("No se puede quitar el autor porque el libro quedaría sin ningún autor asociado.", "libroId: " + libroId);
        }
    }
}
