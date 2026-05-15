package com.libreria.transversal.utilitario;

public final class UtilNumero {

    public static final Integer ENTERO_DEFECTO = 0;
    public static final Double DECIMAL_DEFECTO = 0.0;

    private UtilNumero() {
        super();
    }

    public static boolean esNulo(final Number numero) {
        return UtilObjeto.esNulo(numero);
    }

    public static Integer obtenerValorDefecto(final Integer numero, final Integer valorDefecto) {
        return UtilObjeto.obtenerValorDefecto(numero, valorDefecto);
    }

    public static Integer obtenerValorDefecto(final Integer numero) {
        return obtenerValorDefecto(numero, ENTERO_DEFECTO);
    }

    public static Double obtenerValorDefecto(final Double numero, final Double valorDefecto) {
        return UtilObjeto.obtenerValorDefecto(numero, valorDefecto);
    }

    public static Double obtenerValorDefecto(final Double numero) {
        return obtenerValorDefecto(numero, DECIMAL_DEFECTO);
    }
}