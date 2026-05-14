package com.libreria.datos;

import java.util.List;

public interface ConsultarPorFiltroDAO<E> {

	List<E> consultarPorFiltro(E filtro);

}
