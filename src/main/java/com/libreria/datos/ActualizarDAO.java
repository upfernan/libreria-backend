package com.libreria.datos;

public interface ActualizarDAO<E, I> {

	void actualizar(I id, E entidad);

}
