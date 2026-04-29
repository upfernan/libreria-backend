package com.libreria.entidad;

import java.util.UUID;
import com.libreria.transversal.UtilTexto;

public class EditorialEntidad {

    private UUID id;
    private String nit;
    private String nombre;

    private EditorialEntidad(final Builder builder) {
        setId(builder.id);
        setNit(builder.nit);
        setNombre(builder.nombre);
    }

    public UUID getId() { return id; }
    public String getNit() { return nit; }
    public String getNombre() { return nombre; }

    private void setId(final UUID id) { this.id = id; }
    private void setNit(final String nit) { this.nit = UtilTexto.aplicarTrim(nit); }
    private void setNombre(final String nombre) { this.nombre = UtilTexto.aplicarTrim(nombre); }

    public static class Builder {
        private UUID id;
        private String nit;
        private String nombre;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder nit(final String nit) { this.nit = UtilTexto.aplicarTrim(nit); return this; }
        public Builder nombre(final String nombre) { this.nombre = UtilTexto.aplicarTrim(nombre); return this; }
        public EditorialEntidad build() { return new EditorialEntidad(this); }
    }
}
