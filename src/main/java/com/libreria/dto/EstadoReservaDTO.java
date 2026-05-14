package com.libreria.dto;

import java.util.UUID;
import com.libreria.transversal.utilitario.UtilTexto;

public class EstadoReservaDTO {

    private UUID id;
    private String nombre;
    private String descripcion;

    private EstadoReservaDTO(final Builder builder) {
        setId(builder.id);
        setNombre(builder.nombre);
        setDescripcion(builder.descripcion);
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setNombre(final String nombre) {
        this.nombre = UtilTexto.aplicarTrim(nombre);
    }

    private void setDescripcion(final String descripcion) {
        this.descripcion = UtilTexto.aplicarTrim(descripcion);
    }

    public static class Builder {
        private UUID id;
        private String nombre;
        private String descripcion;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder nombre(final String nombre) {
            this.nombre = UtilTexto.aplicarTrim(nombre);
            return this;
        }

        public Builder descripcion(final String descripcion) {
            this.descripcion = UtilTexto.aplicarTrim(descripcion);
            return this;
        }

        public EstadoReservaDTO build() {
            return new EstadoReservaDTO(this);
        }
    }
}