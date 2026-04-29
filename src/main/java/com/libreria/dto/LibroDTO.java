package com.libreria.dto;

import java.util.UUID;
import com.libreria.transversal.UtilNumero;
import com.libreria.transversal.UtilObjeto;
import com.libreria.transversal.UtilTexto;

public class LibroDTO {

    private UUID id;
    private String titulo;
    private TipoLibroDTO tipoLibro;
    private CategoriaDTO categoria;
    private EditorialDTO editorial;
    private Integer disponibles;

    private LibroDTO(final Builder builder) {
        setId(builder.id);
        setTitulo(builder.titulo);
        setTipoLibro(builder.tipoLibro);
        setCategoria(builder.categoria);
        setEditorial(builder.editorial);
        setDisponibles(builder.disponibles);
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public TipoLibroDTO getTipoLibro() {
        return tipoLibro;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public EditorialDTO getEditorial() {
        return editorial;
    }

    public Integer getDisponibles() {
        return disponibles;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setTitulo(final String titulo) {
        this.titulo = UtilTexto.aplicarTrim(titulo);
    }

    private void setTipoLibro(final TipoLibroDTO tipoLibro) {
        this.tipoLibro = UtilObjeto.obtenerValorDefecto(tipoLibro, new TipoLibroDTO.Builder().build());
    }

    private void setCategoria(final CategoriaDTO categoria) {
        this.categoria = UtilObjeto.obtenerValorDefecto(categoria, new CategoriaDTO.Builder().build());
    }

    private void setEditorial(final EditorialDTO editorial) {
        this.editorial = UtilObjeto.obtenerValorDefecto(editorial, new EditorialDTO.Builder().build());
    }

    private void setDisponibles(final Integer disponibles) {
        this.disponibles = UtilNumero.obtenerValorDefecto(disponibles, 0);
    }

    public static class Builder {
        private UUID id;
        private String titulo;
        private TipoLibroDTO tipoLibro;
        private CategoriaDTO categoria;
        private EditorialDTO editorial;
        private Integer disponibles;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder titulo(final String titulo) {
            this.titulo = UtilTexto.aplicarTrim(titulo);
            return this;
        }

        public Builder tipoLibro(final TipoLibroDTO tipoLibro) {
            this.tipoLibro = UtilObjeto.obtenerValorDefecto(tipoLibro, new TipoLibroDTO.Builder().build());
            return this;
        }

        public Builder categoria(final CategoriaDTO categoria) {
            this.categoria = UtilObjeto.obtenerValorDefecto(categoria, new CategoriaDTO.Builder().build());
            return this;
        }

        public Builder editorial(final EditorialDTO editorial) {
            this.editorial = UtilObjeto.obtenerValorDefecto(editorial, new EditorialDTO.Builder().build());
            return this;
        }

        public Builder disponibles(final Integer disponibles) {
            this.disponibles = UtilNumero.obtenerValorDefecto(disponibles, 0);
            return this;
        }

        public LibroDTO build() {
            return new LibroDTO(this);
        }
    }
}