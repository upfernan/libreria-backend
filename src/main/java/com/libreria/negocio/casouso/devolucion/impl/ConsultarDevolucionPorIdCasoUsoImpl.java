package com.libreria.negocio.casouso.devolucion.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.DevolucionEntidad;
import com.libreria.negocio.casouso.devolucion.ConsultarDevolucionPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarDevolucionPorIdCasoUsoImpl implements ConsultarDevolucionPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarDevolucionPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public DevolucionEntidad ejecutar(final UUID id) {
        // P1 Asegurar que los datos que fueron enviados como filtro sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar devolución en ConsultarDevolucionPorIdCasoUsoImpl.");
        }
        final DevolucionEntidad resultado = daoFactory.getDevolucionDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró la devolución con el identificador proporcionado.", "No existe registro en la tabla devolucion para el id: " + id + " en ConsultarDevolucionPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
