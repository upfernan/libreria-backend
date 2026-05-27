package com.libreria.negocio.casouso.prestamo.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.PrestamoEntidad;
import com.libreria.negocio.casouso.prestamo.ConsultarPrestamoPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarPrestamoPorIdCasoUsoImpl implements ConsultarPrestamoPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarPrestamoPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public PrestamoEntidad ejecutar(final UUID id) {
        // P11 Los datos requeridos deben ser válidos en tipo de dato, longitud, obligatoriedad y formato
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar préstamo en ConsultarPrestamoPorIdCasoUsoImpl.");
        }
        final PrestamoEntidad resultado = daoFactory.getPrestamoDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró el préstamo con el identificador proporcionado.", "No existe registro en la tabla prestamo para el id: " + id + " en ConsultarPrestamoPorIdCasoUsoImpl.");
        }
        return resultado;
    }

}
