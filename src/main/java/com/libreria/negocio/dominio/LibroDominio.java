package com.libreria.negocio.dominio;


import java.util.UUID;
import com.libreria.transversal.utilitario.UtilNumero;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;

public class LibroDominio {

    private UUID id;
    private String titulo;
    private TipoLibroDominio tipoLibro;
    private CategoriaDominio categoria;
    private EditorialDominio editorial;
    private Integer disponibles;

    private LibroDominio(final Builder builder) {
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

    public TipoLibroDominio getTipoLibro() {
        return tipoLibro;
    }

    public CategoriaDominio getCategoria() {
        return categoria;
    }

    public EditorialDominio getEditorial() {
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

    private void setTipoLibro(final TipoLibroDominio tipoLibro) {
        this.tipoLibro = UtilObjeto.obtenerValorDefecto(tipoLibro, new TipoLibroDominio.Builder().build());
    }

    private void setCategoria(final CategoriaDominio categoria) {
        this.categoria = UtilObjeto.obtenerValorDefecto(categoria, new CategoriaDominio.Builder().build());
    }

    private void setEditorial(final EditorialDominio editorial) {
        this.editorial = UtilObjeto.obtenerValorDefecto(editorial, new EditorialDominio.Builder().build());
    }

    private void setDisponibles(final Integer disponibles) {
        this.disponibles = UtilNumero.obtenerValorDefecto(disponibles, 0);
    }

    public static class Builder {
        private UUID id;
        private String titulo;
        private TipoLibroDominio tipoLibro;
        private CategoriaDominio categoria;
        private EditorialDominio editorial;
        private Integer disponibles;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder titulo(final String titulo) {
            this.titulo = UtilTexto.aplicarTrim(titulo);
            return this;
        }

        public Builder tipoLibro(final TipoLibroDominio tipoLibro) {
            this.tipoLibro = UtilObjeto.obtenerValorDefecto(tipoLibro, new TipoLibroDominio.Builder().build());
            return this;
        }

        public Builder categoria(final CategoriaDominio categoria) {
            this.categoria = UtilObjeto.obtenerValorDefecto(categoria, new CategoriaDominio.Builder().build());
            return this;
        }

        public Builder editorial(final EditorialDominio editorial) {
            this.editorial = UtilObjeto.obtenerValorDefecto(editorial, new EditorialDominio.Builder().build());
            return this;
        }

        public Builder disponibles(final Integer disponibles) {
            this.disponibles = UtilNumero.obtenerValorDefecto(disponibles, 0);
            return this;
        }

        public LibroDominio build() {
            return new LibroDominio(this);
        }
    }
}
