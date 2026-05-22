package com.libreria.entidad;

import java.util.UUID;

import com.libreria.transversal.utilitario.UtilObjeto;

public class AutorLibroEntidad {

    private UUID id;
    private AutorEntidad autor;
    private LibroEntidad libro;

    private AutorLibroEntidad(final Builder builder) {
        setId(builder.id);
        setAutor(builder.autor);
        setLibro(builder.libro);
    }

    public UUID getId() { return id; }
    public AutorEntidad getAutor() { return autor; }
    public LibroEntidad getLibro() { return libro; }

    private void setId(final UUID id) { this.id = id; }
    private void setAutor(final AutorEntidad autor) { this.autor = UtilObjeto.obtenerValorDefecto(autor, new AutorEntidad.Builder().build()); }
    private void setLibro(final LibroEntidad libro) { this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroEntidad.Builder().build()); }

    public static class Builder {
        private UUID id;
        private AutorEntidad autor;
        private LibroEntidad libro;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder autor(final AutorEntidad autor) { this.autor = UtilObjeto.obtenerValorDefecto(autor, new AutorEntidad.Builder().build()); return this; }
        public Builder libro(final LibroEntidad libro) { this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroEntidad.Builder().build()); return this; }
        public AutorLibroEntidad build() { return new AutorLibroEntidad(this); }
    }
}
