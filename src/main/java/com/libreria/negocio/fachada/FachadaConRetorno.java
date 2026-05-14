package com.libreria.negocio.fachada;

public interface FachadaConRetorno<E, S> {

	S ejecutar(E datos);

}
