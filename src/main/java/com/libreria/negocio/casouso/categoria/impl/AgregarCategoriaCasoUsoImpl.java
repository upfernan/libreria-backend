package com.libreria.negocio.casouso.categoria.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.negocio.casouso.categoria.AgregarCategoriaCasoUso;
import com.libreria.negocio.dominio.CategoriaDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarCategoriaCasoUsoImpl implements AgregarCategoriaCasoUso {

    private final DAOFactory daoFactory;

    public AgregarCategoriaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final CategoriaDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P2 — Validar que no exista otra categoría con el mismo nombre
        validarNombreUnico(datos.getNombre());
        // P1 — Registrar la categoría garantizando identificador único
        guardarNuevaCategoria(datos);
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final CategoriaDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la categoría son obligatorios.", "Se recibió un objeto CategoriaDominio nulo.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre de la categoría es obligatorio.", "El campo nombre llegó nulo o vacío en CategoriaDominio.");
        }
        if (UtilTexto.esNula(datos.getDescripcion())) {
            throw GestorLibreriaExcepcion.crear("La descripción de la categoría es obligatoria.", "El campo descripcion llegó nulo o vacío en CategoriaDominio.");
        }
    }

    // P2 — Validar que no exista otra categoría con el mismo nombre
    private void validarNombreUnico(final String nombre) {
        final CategoriaEntidad filtro = new CategoriaEntidad.Builder().nombre(nombre).build();
        final List<CategoriaEntidad> existentes = daoFactory.getCategoriaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("Ya existe una categoría con ese nombre.", "nombre duplicado en categoria: " + nombre);
        }
    }

    // P1 — Generar id único y persistir la nueva categoría
    private void guardarNuevaCategoria(final CategoriaDominio datos) {
        UUID id = UtilUUID.generar();
        while (UtilUUID.tieneValor(daoFactory.getCategoriaDAO().consultarPorId(id).getId())) {
            id = UtilUUID.generar();
        }
        final CategoriaEntidad nueva = new CategoriaEntidad.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        daoFactory.getCategoriaDAO().crear(nueva);
    }
}
