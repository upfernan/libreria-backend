package com.libreria.negocio.casouso.signatura.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.ConsultarSignaturaPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarSignaturaPorIdCasoUsoImpl implements ConsultarSignaturaPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarSignaturaPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public SignaturaEntidad ejecutar(final UUID id) {
        // P7 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        if (!UtilUUID.tieneValor(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo o inválido para consultar signatura en ConsultarSignaturaPorIdCasoUsoImpl.");
        }
        final SignaturaEntidad resultado = daoFactory.getSignaturaDAO().consultarPorId(id);
        if (!UtilUUID.tieneValor(resultado.getId())) {
            throw GestorLibreriaExcepcion.crear("No se encontró la signatura con el identificador proporcionado.", "No existe registro en la tabla signatura para el id: " + id + " en ConsultarSignaturaPorIdCasoUsoImpl.");
        }
        return resultado;
    }
}
