package com.libreria.negocio.casouso.categoria.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.CategoriaEntidad;
import com.libreria.negocio.casouso.categoria.ConsultarCategoriaPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarCategoriaPorIdCasoUsoImpl implements ConsultarCategoriaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarCategoriaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public CategoriaEntidad ejecutar(final UUID id) {
        // P6 — Validar que el identificador sea obligatorio
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo para consultar categoría.");
        }
        return daoFactory.getCategoriaDAO().consultarPorId(id);
    }
}
