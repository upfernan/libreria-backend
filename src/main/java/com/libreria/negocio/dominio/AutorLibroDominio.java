package com.libreria.negocio.dominio;

import java.util.UUID;
import com.libreria.transversal.utilitario.UtilObjeto;

public class AutorLibroDominio {

    private UUID id;
    private AutorDominio autor;
    private LibroDominio libro;

    private AutorLibroDominio(final Builder builder) {
        setId(builder.id);
        setAutor(builder.autor);
        setLibro(builder.libro);
    }

    public UUID getId() {
        return id;
    }

    public AutorDominio getAutor() {
        return autor;
    }

    public LibroDominio getLibro() {
        return libro;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setAutor(final AutorDominio autor) {
        this.autor = UtilObjeto.obtenerValorDefecto(autor, new AutorDominio.Builder().build());
    }

    private void setLibro(final LibroDominio libro) {
        this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDominio.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private AutorDominio autor;
        private LibroDominio libro;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder autor(final AutorDominio autor) {
            this.autor = UtilObjeto.obtenerValorDefecto(autor, new AutorDominio.Builder().build());
            return this;
        }

        public Builder libro(final LibroDominio libro) {
            this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDominio.Builder().build());
            return this;
        }

        public AutorLibroDominio build() {
            return new AutorLibroDominio(this);
        }
    }
}