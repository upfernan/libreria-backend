package com.libreria.transversal.utilitario;

import java.util.UUID;

public final class UtilUUID {

    public static final UUID UUID_DEFECTO = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final UUID SIGNATURA_POR_DEFECTO = UUID.fromString("00000000-0000-0000-0005-000000000001");

    private UtilUUID() {
        super();
    }

    public static boolean esNulo(final UUID id) {
        return UtilObjeto.esNulo(id);
    }

    public static UUID obtenerValorDefecto(final UUID id, final UUID valorDefecto) {
        return UtilObjeto.obtenerValorDefecto(id, valorDefecto);
    }

    public static UUID obtenerValorDefecto(final UUID id) {
        return obtenerValorDefecto(id, UUID_DEFECTO);
    }

    public static UUID generar() {
        return UUID.randomUUID();
    }

    public static boolean tieneValor(final UUID id) {
        return !esNulo(id) && !UUID_DEFECTO.equals(id);
    }
}