package com.libreria.negocio.dominio;

import java.util.UUID;
import com.libreria.transversal.utilitario.UtilTexto;

public class AutorDominio {

    private UUID id;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;

    private AutorDominio(final Builder builder) {
        setId(builder.id);
        setPrimerNombre(builder.primerNombre);
        setSegundoNombre(builder.segundoNombre);
        setPrimerApellido(builder.primerApellido);
        setSegundoApellido(builder.segundoApellido);
    }

    public UUID getId() {
        return id;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setPrimerNombre(final String primerNombre) {
        this.primerNombre = UtilTexto.aplicarTrim(primerNombre);
    }

    private void setSegundoNombre(final String segundoNombre) {
        this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre);
    }

    private void setPrimerApellido(final String primerApellido) {
        this.primerApellido = UtilTexto.aplicarTrim(primerApellido);
    }

    private void setSegundoApellido(final String segundoApellido) {
        this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido);
    }

    public static class Builder {
        private UUID id;
        private String primerNombre;
        private String segundoNombre;
        private String primerApellido;
        private String segundoApellido;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder primerNombre(final String primerNombre) {
            this.primerNombre = UtilTexto.aplicarTrim(primerNombre);
            return this;
        }

        public Builder segundoNombre(final String segundoNombre) {
            this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre);
            return this;
        }

        public Builder primerApellido(final String primerApellido) {
            this.primerApellido = UtilTexto.aplicarTrim(primerApellido);
            return this;
        }

        public Builder segundoApellido(final String segundoApellido) {
            this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido);
            return this;
        }

        public AutorDominio build() {
            return new AutorDominio(this);
        }
    }
}
