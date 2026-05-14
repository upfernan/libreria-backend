package com.libreria.datos.dao;

import java.util.UUID;

import com.libreria.datos.ActualizarDAO;
import com.libreria.datos.ConsultarPorFiltroDAO;
import com.libreria.datos.ConsultarPorIdDAO;
import com.libreria.datos.ConsultarTodosDAO;
import com.libreria.datos.CrearDAO;
import com.libreria.datos.EliminarDAO;
import com.libreria.entidad.EstadoReservaEntidad;

public interface EstadoReservaDAO extends
		CrearDAO<EstadoReservaEntidad>,
		ConsultarTodosDAO<EstadoReservaEntidad>,
		ConsultarPorIdDAO<EstadoReservaEntidad, UUID>,
		ConsultarPorFiltroDAO<EstadoReservaEntidad>,
		ActualizarDAO<EstadoReservaEntidad, UUID>,
		EliminarDAO<UUID> {

}
