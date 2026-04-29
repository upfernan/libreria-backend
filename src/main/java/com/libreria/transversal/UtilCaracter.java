package com.libreria.transversal;

public final class UtilCaracter {

    public static final char CARACTER_DEFECTO = ' ';

    private UtilCaracter() {
        super();
    }

    public static char obtenerValorDefecto(final char valor) {
        return valor == '\0' ? CARACTER_DEFECTO : valor;
    }

    public static char obtenerValorDefecto(final char valor, final char valorDefecto) {
        return valor == '\0' ? valorDefecto : valor;
    }

    public static boolean esVacio(final char valor) {
        return valor == '\0' || valor == CARACTER_DEFECTO;
    }
}
