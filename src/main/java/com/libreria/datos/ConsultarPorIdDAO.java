package com.libreria.datos;

public interface ConsultarPorIdDAO<E, I> {

	E consultarPorId(I id);

}
