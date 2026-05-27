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
        // P6 Asegura que los datos requeridos sean validos a nivel de tipo, formato y obligatoriedad
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar categoría en ConsultarCategoriaPorIdCasoUsoImpl.");
        }
        final CategoriaEntidad resultado = daoFactory.getCategoriaDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró la categoría con el identificador proporcionado.", "No existe registro en la tabla categoria para el id: " + id + " en ConsultarCategoriaPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
