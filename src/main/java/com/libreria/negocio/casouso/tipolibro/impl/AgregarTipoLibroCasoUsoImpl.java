package com.libreria.negocio.casouso.tipolibro.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoLibroEntidad;
import com.libreria.negocio.casouso.tipolibro.AgregarTipoLibroCasoUso;
import com.libreria.negocio.dominio.TipoLibroDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarTipoLibroCasoUsoImpl implements AgregarTipoLibroCasoUso {

    private final DAOFactory daoFactory;

    public AgregarTipoLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final TipoLibroDominio datos) {
        // P3  Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P2  Validar que no exista otro tipo de libro con el mismo nombre
        validarNombreUnico(datos.getNombre());
        // P1 No peude existir un tipo libro con mismo id
        guardarNuevoTipoLibro(datos);
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final TipoLibroDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del tipo de libro son obligatorios.", "Se recibió un objeto TipoLibroDominio nulo.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre del tipo de libro es obligatorio.", "El campo nombre llegó nulo o vacío en TipoLibroDominio.");
        }
        if (UtilTexto.esNula(datos.getDescripcion())) {
            throw GestorLibreriaExcepcion.crear("La descripción del tipo de libro es obligatoria.", "El campo descripcion llegó nulo o vacío en TipoLibroDominio.");
        }
    }

    // P2 — Validar que no exista otro tipo de libro con el mismo nombre
    private void validarNombreUnico(final String nombre) {
        final TipoLibroEntidad filtro = new TipoLibroEntidad.Builder().nombre(nombre).build();
        final List<TipoLibroEntidad> existentes = daoFactory.getTipoLibroDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("Ya existe un tipo de libro con ese nombre.", "nombre duplicado en tipoLibro: " + nombre);
        }
    }

    // P1 — Generar id único y persistir el nuevo tipo de libro
    private void guardarNuevoTipoLibro(final TipoLibroDominio datos) {
        UUID id = UtilUUID.generar();
        while (UtilUUID.tieneValor(daoFactory.getTipoLibroDAO().consultarPorId(id).getId())) {
            id = UtilUUID.generar();
        }
        final TipoLibroEntidad nueva = new TipoLibroEntidad.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        daoFactory.getTipoLibroDAO().crear(nueva);
    }
}
