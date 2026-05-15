package com.libreria.transversal.utilitario;

public final class UtilTexto {

    public static final String VACIO = "";

    private UtilTexto() {
        super();
    }

    public static boolean esNula(final String texto) {
        return UtilObjeto.esNulo(texto);
    }

    public static String obtenerValorDefecto(final String texto, final String valorDefecto) {
        return UtilObjeto.obtenerValorDefecto(texto, valorDefecto);
    }

    public static String obtenerValorDefecto(final String texto) {
        return obtenerValorDefecto(texto, VACIO);
    }

    public static String aplicarTrim(final String texto) {
        return obtenerValorDefecto(texto).trim();
    }

    public static boolean soloLetrasYEspacios(final String texto) {
        return !esNula(texto) && aplicarTrim(texto).matches("^[\\p{L} ]+$");
    }

    public static boolean soloLetrasMayusculasYEspacios(final String texto) {
        return !esNula(texto) && aplicarTrim(texto).matches("^[\\p{Lu} ]+$");
    }

    public static boolean tieneLongitudValida(final String texto, final int minimo, final int maximo) {
        return !esNula(texto) && aplicarTrim(texto).length() >= minimo && aplicarTrim(texto).length() <= maximo;
    }
}
