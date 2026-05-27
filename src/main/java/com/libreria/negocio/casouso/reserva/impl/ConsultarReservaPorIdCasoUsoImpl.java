package com.libreria.negocio.casouso.reserva.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.ReservaEntidad;
import com.libreria.negocio.casouso.reserva.ConsultarReservaPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarReservaPorIdCasoUsoImpl implements ConsultarReservaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarReservaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public ReservaEntidad ejecutar(final UUID id) {
        // P9 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar reserva en ConsultarReservaPorIdCasoUsoImpl.");
        }
        final ReservaEntidad resultado = daoFactory.getReservaDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró la reserva con el identificador proporcionado.", "No existe registro en la tabla reserva para el id: " + id + " en ConsultarReservaPorIdCasoUsoImpl.");
        }
        return resultado;
    }

}
