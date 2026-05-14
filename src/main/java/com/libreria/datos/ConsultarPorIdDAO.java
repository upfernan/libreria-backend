package com.libreria.datos;

public interface ConsultarPorIdDAO<E, ID> {

	E consultarPorId(ID id);

}
