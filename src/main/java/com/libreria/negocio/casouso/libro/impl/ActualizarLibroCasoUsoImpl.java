package com.libreria.negocio.casouso.libro.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.libro.ActualizarLibroCasoUso;
import com.libreria.negocio.dominio.LibroDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarLibroCasoUsoImpl implements ActualizarLibroCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final LibroDominio datos) {
        // P5 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P6 — Validar que el libro exista en el sistema
        validarExistenciaLibro(datos.getId());
        // P2 — Validar que la categoría existe en el sistema
        validarExistenciaCategoria(datos.getCategoria().getId());
        // P3 — Validar que el tipo de libro existe en el sistema
        // IMPORTANTE NO MOVER: el tipo de libro determina la signatura al crear ejemplares
        // cambiar el tipoLibro de un libro existente no modifica las signaturas ya asignadas a sus ejemplares.
        validarExistenciaTipoLibro(datos.getTipoLibro().getId());
        // P4 — Validar que la editorial existe en el sistema
        validarExistenciaEditorial(datos.getEditorial().getId());
        // P1 — Actualizar el libro en el sistema
        daoFactory.getLibroDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P5 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final LibroDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del libro son obligatorios.", "Se recibió un objeto LibroDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador del libro es obligatorio.", "El campo id llegó nulo en LibroDominio.");
        }
        if (UtilTexto.esNula(datos.getTitulo())) {
            throw GestorLibreriaExcepcion.crear("El título del libro es obligatorio.", "El campo titulo llegó nulo o vacío en LibroDominio.");
        }
        if (UtilObjeto.esNulo(datos.getTipoLibro()) || UtilUUID.esNulo(datos.getTipoLibro().getId())) {
            throw GestorLibreriaExcepcion.crear("El tipo de libro es obligatorio.", "El campo tipoLibroId llegó nulo o vacío en LibroDominio.");
        }
        if (datos.getTipoLibro().getId().equals(UtilUUID.UUID_DEFECTO)) {
            throw GestorLibreriaExcepcion.crear("El tipo de libro no es válido.", "El tipoLibroId es igual al UUID_DEFECTO.");
        }
        if (UtilObjeto.esNulo(datos.getCategoria()) || UtilUUID.esNulo(datos.getCategoria().getId())) {
            throw GestorLibreriaExcepcion.crear("La categoría del libro es obligatoria.", "El campo categoriaId llegó nulo o vacío en LibroDominio.");
        }
        if (datos.getCategoria().getId().equals(UtilUUID.UUID_DEFECTO)) {
            throw GestorLibreriaExcepcion.crear("La categoría del libro no es válida.", "El categoriaId es igual al UUID_DEFECTO.");
        }
        if (UtilObjeto.esNulo(datos.getEditorial()) || UtilUUID.esNulo(datos.getEditorial().getId())) {
            throw GestorLibreriaExcepcion.crear("La editorial del libro es obligatoria.", "El campo editorialId llegó nulo o vacío en LibroDominio.");
        }
        if (datos.getEditorial().getId().equals(UtilUUID.UUID_DEFECTO)) {
            throw GestorLibreriaExcepcion.crear("La editorial del libro no es válida.", "El editorialId es igual al UUID_DEFECTO.");
        }
        if (UtilObjeto.esNulo(datos.getDisponibles()) || datos.getDisponibles() < 0) {
            throw GestorLibreriaExcepcion.crear("La cantidad de disponibles debe ser un número mayor o igual a cero.", "El campo disponibles llegó nulo o negativo en LibroDominio.");
        }
    }

    // P6 — Validar que el libro exista en la base de datos
    private void validarExistenciaLibro(final UUID id) {
        final LibroEntidad entidad = daoFactory.getLibroDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El libro indicado no existe en el sistema.", "No se encontró libro con id: " + id);
        }
    }

    // P2 — Validar que la categoría exista en la base de datos
    private void validarExistenciaCategoria(final UUID categoriaId) {
        final com.libreria.entidad.CategoriaEntidad categoria = daoFactory.getCategoriaDAO().consultarPorId(categoriaId);
        if (UtilObjeto.esNulo(categoria) || UtilObjeto.esNulo(categoria.getId())) {
            throw GestorLibreriaExcepcion.crear("La categoría indicada no existe en el sistema.", "No se encontró registro de categoria con id: " + categoriaId);
        }
    }

    // P3 — Validar que el tipo de libro exista en la base de datos
    private void validarExistenciaTipoLibro(final UUID tipoLibroId) {
        final TipoLibroEntidad tipoLibro = daoFactory.getTipoLibroDAO().consultarPorId(tipoLibroId);
        if (UtilObjeto.esNulo(tipoLibro) || UtilObjeto.esNulo(tipoLibro.getId())) {
            throw GestorLibreriaExcepcion.crear("El tipo de libro indicado no existe en el sistema.", "No se encontró registro de tipoLibro con id: " + tipoLibroId);
        }
    }

    // P4 — Validar que la editorial exista en la base de datos
    private void validarExistenciaEditorial(final UUID editorialId) {
        final EditorialEntidad editorial = daoFactory.getEditorialDAO().consultarPorId(editorialId);
        if (UtilObjeto.esNulo(editorial) || UtilObjeto.esNulo(editorial.getId())) {
            throw GestorLibreriaExcepcion.crear("La editorial indicada no existe en el sistema.", "No se encontró registro de editorial con id: " + editorialId);
        }
    }

    private LibroEntidad construirEntidad(final LibroDominio datos) {
        return new LibroEntidad.Builder()
                .id(datos.getId())
                .titulo(datos.getTitulo())
                .tipoLibro(new TipoLibroEntidad.Builder()
                        .id(datos.getTipoLibro().getId())
                        .build())
                .categoria(new CategoriaEntidad.Builder()
                        .id(datos.getCategoria().getId())
                        .build())
                .editorial(new EditorialEntidad.Builder()
                        .id(datos.getEditorial().getId())
                        .build())
                .disponibles(datos.getDisponibles())
                .build();
    }

}
