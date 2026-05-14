package com.libreria.transversal.utilitario;

public final class UtilBooleano {

    public static final Boolean FALSO = Boolean.FALSE;

    private UtilBooleano() {
        super();
    }

    public static boolean esNulo(final Boolean valor) {
        return UtilObjeto.esNulo(valor);
    }

    public static Boolean obtenerValorDefecto(final Boolean valor, final Boolean valorDefecto) {
        return UtilObjeto.obtenerValorDefecto(valor, valorDefecto);
    }

    public static Boolean obtenerValorDefecto(final Boolean valor) {
        return obtenerValorDefecto(valor, FALSO);
    }
}