package com.libreria.negocio.casouso;

public interface CasoUsoConRetorno<E, S> {

	S ejecutar(E datos);

}
