package com.libreria.transversal;

import java.time.LocalDate;

public final class UtilFecha {

    public static final LocalDate FECHA_DEFECTO = LocalDate.of(1900, 1, 1);

    private UtilFecha() {
        super();
    }

    public static boolean esNula(final LocalDate fecha) {
        return UtilObjeto.esNulo(fecha);
    }

    public static LocalDate obtenerValorDefecto(final LocalDate fecha, final LocalDate valorDefecto) {
        return UtilObjeto.obtenerValorDefecto(fecha, valorDefecto);
    }

    public static LocalDate obtenerValorDefecto(final LocalDate fecha) {
        return obtenerValorDefecto(fecha, FECHA_DEFECTO);
    }
}
