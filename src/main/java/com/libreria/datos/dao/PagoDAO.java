package com.libreria.datos.dao;

import java.util.UUID;

import com.libreria.datos.ActualizarDAO;
import com.libreria.datos.ConsultarPorFiltroDAO;
import com.libreria.datos.ConsultarPorIdDAO;
import com.libreria.datos.ConsultarTodosDAO;
import com.libreria.datos.CrearDAO;
import com.libreria.datos.EliminarDAO;
import com.libreria.entidad.PagoEntidad;

public interface PagoDAO extends
		CrearDAO<PagoEntidad>,
		ConsultarTodosDAO<PagoEntidad>,
		ConsultarPorIdDAO<PagoEntidad, UUID>,
		ConsultarPorFiltroDAO<PagoEntidad>,
		ActualizarDAO<PagoEntidad, UUID>,
		EliminarDAO<UUID> {

}
