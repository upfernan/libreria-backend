package com.libreria.entidad;

import java.util.UUID;

import com.libreria.transversal.utilitario.UtilNumero;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;

public class LibroEntidad {

    private UUID id;
    private String titulo;
    private TipoLibroEntidad tipoLibro;
    private CategoriaEntidad categoria;
    private EditorialEntidad editorial;
    private Integer disponibles;

    private LibroEntidad(final Builder builder) {
        setId(builder.id);
        setTitulo(builder.titulo);
        setTipoLibro(builder.tipoLibro);
        setCategoria(builder.categoria);
        setEditorial(builder.editorial);
        setDisponibles(builder.disponibles);
    }

    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public TipoLibroEntidad getTipoLibro() { return tipoLibro; }
    public CategoriaEntidad getCategoria() { return categoria; }
    public EditorialEntidad getEditorial() { return editorial; }
    public Integer getDisponibles() { return disponibles; }

    private void setId(final UUID id) { this.id = id; }
    private void setTitulo(final String titulo) { this.titulo = UtilTexto.aplicarTrim(titulo); }
    private void setTipoLibro(final TipoLibroEntidad tipoLibro) { this.tipoLibro = UtilObjeto.obtenerValorDefecto(tipoLibro, new TipoLibroEntidad.Builder().build()); }
    private void setCategoria(final CategoriaEntidad categoria) { this.categoria = UtilObjeto.obtenerValorDefecto(categoria, new CategoriaEntidad.Builder().build()); }
    private void setEditorial(final EditorialEntidad editorial) { this.editorial = UtilObjeto.obtenerValorDefecto(editorial, new EditorialEntidad.Builder().build()); }
    private void setDisponibles(final Integer disponibles) { this.disponibles = UtilNumero.obtenerValorDefecto(disponibles, 0); }


    public boolean isIdValorPorDefecto() {
        return UtilUUID.esValorDefecto(id);
    }
    public static class Builder {
        private UUID id;
        private String titulo;
        private TipoLibroEntidad tipoLibro;
        private CategoriaEntidad categoria;
        private EditorialEntidad editorial;
        private Integer disponibles;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder titulo(final String titulo) { this.titulo = UtilTexto.aplicarTrim(titulo); return this; }
        public Builder tipoLibro(final TipoLibroEntidad tipoLibro) { this.tipoLibro = UtilObjeto.obtenerValorDefecto(tipoLibro, new TipoLibroEntidad.Builder().build()); return this; }
        public Builder categoria(final CategoriaEntidad categoria) { this.categoria = UtilObjeto.obtenerValorDefecto(categoria, new CategoriaEntidad.Builder().build()); return this; }
        public Builder editorial(final EditorialEntidad editorial) { this.editorial = UtilObjeto.obtenerValorDefecto(editorial, new EditorialEntidad.Builder().build()); return this; }
        public Builder disponibles(final Integer disponibles) { this.disponibles = UtilNumero.obtenerValorDefecto(disponibles, 0); return this; }
        public LibroEntidad build() { return new LibroEntidad(this); }
    }
}
