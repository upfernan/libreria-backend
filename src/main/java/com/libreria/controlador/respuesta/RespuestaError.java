package com.libreria.controlador.respuesta;

import java.time.LocalDateTime;

public record RespuestaError(String mensaje, LocalDateTime fecha) {

    public static RespuestaError crear(String mensaje) {
        return new RespuestaError(mensaje.trim(), LocalDateTime.now());
    }
}
