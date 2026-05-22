package com.libreria.negocio.casouso.usuario.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.UsuarioEntidad;
import com.libreria.negocio.casouso.usuario.ConsultarTodosUsuariosCasoUso;

public class ConsultarTodosUsuariosCasoUsoImpl implements ConsultarTodosUsuariosCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosUsuariosCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public List<UsuarioEntidad> ejecutar(final UsuarioEntidad filtro) {
        // p9 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
        return daoFactory.getUsuarioDAO().consultarPorFiltro(filtro);
    }

}
