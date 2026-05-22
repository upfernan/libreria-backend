package com.libreria.negocio.casouso.autorlibro.impl;

import java.util.List;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorLibroEntidad;
import com.libreria.negocio.casouso.autorlibro.ConsultarTodosAutoresLibroCasoUso;

public class ConsultarTodosAutoresLibroCasoUsoImpl implements ConsultarTodosAutoresLibroCasoUso {

    private final DAOFactory daoFactory;

    public ConsultarTodosAutoresLibroCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }
// p7 Asegurar que los datos que fueron enviados como filtro para llevar a cabo la acción sean válidos a nivel de tipo de dato, longitud, obligatoriedad, formato y rango.
    @Override
    public List<AutorLibroEntidad> ejecutar(final AutorLibroEntidad filtro) {
        return daoFactory.getAutorLibroDAO().consultarTodos();
    }
}
