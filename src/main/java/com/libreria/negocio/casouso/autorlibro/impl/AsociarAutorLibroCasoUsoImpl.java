package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorEntidad;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.entidad.LibroEntidad;
import com.libreria.negocio.casouso.autorlibro.AsociarAutorLibroCasoUso;
import com.libreria.negocio.dominio.AutorLibroDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AsociarAutorLibroCasoUsoImpl implements AsociarAutorLibroCasoUso {

    private final DAOFactory daoFactory;

    public AsociarAutorLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final AutorLibroDominio datos) {
        // P5 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P2 — Validar que el libro exista en el sistema
        validarLibroExiste(datos.getLibro().getId());
        // P3 — Validar que el autor exista en el sistema
        validarAutorExiste(datos.getAutor().getId());
        // P4 — Validar que no exista ya una relación entre ese autor y ese libro
        validarCombinacionUnica(datos.getAutor().getId(), datos.getLibro().getId());
        // P1 — Registrar la relación autor-libro garantizando identificador único
        guardarNuevaRelacion(datos);
    }

    // P5 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final AutorLibroDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la relación autor-libro son obligatorios.", "Se recibió un objeto AutorLibroDominio nulo.");
        }
        if (UtilObjeto.esNulo(datos.getAutor()) || !UtilUUID.tieneValor(datos.getAutor().getId())) {
            throw GestorLibreriaExcepcion.crear("El autor de la relación es obligatorio.", "El campo autor o su id no tiene un valor válido en AutorLibroDominio.");
        }
        if (UtilObjeto.esNulo(datos.getLibro()) || !UtilUUID.tieneValor(datos.getLibro().getId())) {
            throw GestorLibreriaExcepcion.crear("El libro de la relación es obligatorio.", "El campo libro o su id no tiene un valor válido en AutorLibroDominio.");
        }
    }

    // P2 — El libro debe estar registrado en el sistema
    private void validarLibroExiste(final UUID libroId) {
        final LibroEntidad entidad = daoFactory.getLibroDAO().consultarPorId(libroId);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El libro indicado no existe en el sistema.", "No se encontró Libro con id: " + libroId);
        }
    }

    // P3 — El autor debe estar registrado en el sistema
    private void validarAutorExiste(final UUID autorId) {
        final AutorEntidad entidad = daoFactory.getAutorDAO().consultarPorId(autorId);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("El autor indicado no existe en el sistema.", "No se encontró Autor con id: " + autorId);
        }
    }

    // P4 — No puede existir otra relación con el mismo autor y libro
    private void validarCombinacionUnica(final UUID autorId, final UUID libroId) {
        final AutorLibroEntidad filtro = new AutorLibroEntidad.Builder()
                .autor(new AutorEntidad.Builder().id(autorId).build())
                .libro(new LibroEntidad.Builder().id(libroId).build())
                .build();
        final List<AutorLibroEntidad> existentes = daoFactory.getAutorLibroDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("Ya existe una relación registrada entre ese autor y ese libro.", "Combinación duplicada en autorLibro: autorId=" + autorId + ", libroId=" + libroId);
        }
    }

    // P1 — Generar id único y persistir la nueva relación autor-libro
    private void guardarNuevaRelacion(final AutorLibroDominio datos) {
        UUID id = UtilUUID.generar();
        while (!UtilObjeto.esNulo(daoFactory.getAutorLibroDAO().consultarPorId(id))) {
            id = UtilUUID.generar();
        }
        final AutorLibroEntidad nueva = new AutorLibroEntidad.Builder()
                .id(id)
                .autor(new AutorEntidad.Builder().id(datos.getAutor().getId()).build())
                .libro(new LibroEntidad.Builder().id(datos.getLibro().getId()).build())
                .build();
        daoFactory.getAutorLibroDAO().crear(nueva);
    }
}
