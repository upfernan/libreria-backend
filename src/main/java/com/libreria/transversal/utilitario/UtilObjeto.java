package com.libreria.transversal.utilitario;

public final class UtilObjeto {

    private UtilObjeto() {
        super();
    }

    public static <O> boolean esNulo(final O objeto) {
        return objeto == null;
    }

    public static <O> O obtenerValorDefecto(final O objeto, final O valorDefecto) {
        return esNulo(objeto) ? valorDefecto : objeto;
    }
}
