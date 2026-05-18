package com.libreria.dto;

import java.util.UUID;
import com.libreria.transversal.utilitario.UtilObjeto;

public class AutorLibroDTO {

    private UUID id;
    private AutorDTO autor;
    private LibroDTO libro;

    private AutorLibroDTO(final Builder builder) {
        setId(builder.id);
        setAutor(builder.autor);
        setLibro(builder.libro);
    }

    public AutorLibroDTO() {
        setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        setAutor(null);
        setLibro(null);
    }

    public UUID getId() {
        return id;
    }

    public AutorDTO getAutor() {
        return autor;
    }

    public LibroDTO getLibro() {
        return libro;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setAutor(final AutorDTO autor) {
        this.autor = UtilObjeto.obtenerValorDefecto(autor, new AutorDTO.Builder().build());
    }

    private void setLibro(final LibroDTO libro) {
        this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDTO.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private AutorDTO autor;
        private LibroDTO libro;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder autor(final AutorDTO autor) {
            this.autor = UtilObjeto.obtenerValorDefecto(autor, new AutorDTO.Builder().build());
            return this;
        }

        public Builder libro(final LibroDTO libro) {
            this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDTO.Builder().build());
            return this;
        }

        public AutorLibroDTO build() {
            return new AutorLibroDTO(this);
        }
    }
}
