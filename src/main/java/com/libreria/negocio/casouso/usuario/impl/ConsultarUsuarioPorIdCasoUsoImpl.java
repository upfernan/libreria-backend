package com.libreria.negocio.casouso.usuario.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.usuario.ConsultarUsuarioPorIdCasoUso;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ConsultarUsuarioPorIdCasoUsoImpl implements ConsultarUsuarioPorIdCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarUsuarioPorIdCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public UsuarioEntidad ejecutar(final UUID id) {
        // p9 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        if (UtilUUID.esNulo(id)) {
            throw GestorLibreriaExcepcion.crear("El identificador es obligatorio.", "Se recibió un UUID nulo para consultar usuario.");
        }
        return daoFactory.getUsuarioDAO().consultarPorId(id);
    }

}
