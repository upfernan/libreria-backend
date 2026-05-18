package com.libreria.controlador.respuesta;

import java.time.LocalDateTime;

public record RespuestaExito<T>(String mensaje, LocalDateTime fecha, T datos) {

    public static <T> RespuestaExito<T> crear(String mensaje, T datos) {
        return new RespuestaExito<>(mensaje.trim(), LocalDateTime.now(), datos);
    }
}
