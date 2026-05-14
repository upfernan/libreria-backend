package com.libreria.datos.dao;

import java.util.UUID;

import com.libreria.datos.ActualizarDAO;
import com.libreria.datos.ConsultarPorFiltroDAO;
import com.libreria.datos.ConsultarPorIdDAO;
import com.libreria.datos.ConsultarTodosDAO;
import com.libreria.datos.CrearDAO;
import com.libreria.datos.EliminarDAO;
import com.libreria.entidad.PrestamoEntidad;

public interface PrestamoDAO extends
		CrearDAO<PrestamoEntidad>,
		ConsultarTodosDAO<PrestamoEntidad>,
		ConsultarPorIdDAO<PrestamoEntidad, UUID>,
		ConsultarPorFiltroDAO<PrestamoEntidad>,
		ActualizarDAO<PrestamoEntidad, UUID>,
		EliminarDAO<UUID> {

}
