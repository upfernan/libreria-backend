package com.libreria.entidad;

import java.util.UUID;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;

public class UsuarioEntidad {

    private UUID id;
    private TipoIdentificacionEntidad tipoIdentificacion;
    private String numeroIdentificacion;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String correoElectronico;

    private UsuarioEntidad(final Builder builder) {
        setId(builder.id);
        setTipoIdentificacion(builder.tipoIdentificacion);
        setNumeroIdentificacion(builder.numeroIdentificacion);
        setPrimerNombre(builder.primerNombre);
        setSegundoNombre(builder.segundoNombre);
        setPrimerApellido(builder.primerApellido);
        setSegundoApellido(builder.segundoApellido);
        setCorreoElectronico(builder.correoElectronico);
    }

    public UUID getId() { return id; }
    public TipoIdentificacionEntidad getTipoIdentificacion() { return tipoIdentificacion; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public String getPrimerNombre() { return primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public String getCorreoElectronico() { return correoElectronico; }

    private void setId(final UUID id) { this.id = id; }
    private void setTipoIdentificacion(final TipoIdentificacionEntidad tipoIdentificacion) { this.tipoIdentificacion = UtilObjeto.obtenerValorDefecto(tipoIdentificacion, new TipoIdentificacionEntidad.Builder().build()); }
    private void setNumeroIdentificacion(final String numeroIdentificacion) { this.numeroIdentificacion = UtilTexto.aplicarTrim(numeroIdentificacion); }
    private void setPrimerNombre(final String primerNombre) { this.primerNombre = UtilTexto.aplicarTrim(primerNombre); }
    private void setSegundoNombre(final String segundoNombre) { this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre); }
    private void setPrimerApellido(final String primerApellido) { this.primerApellido = UtilTexto.aplicarTrim(primerApellido); }
    private void setSegundoApellido(final String segundoApellido) { this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido); }
    private void setCorreoElectronico(final String correoElectronico) { this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico); }

    public static class Builder {
        private UUID id;
        private TipoIdentificacionEntidad tipoIdentificacion;
        private String numeroIdentificacion;
        private String primerNombre;
        private String segundoNombre;
        private String primerApellido;
        private String segundoApellido;
        private String correoElectronico;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder tipoIdentificacion(final TipoIdentificacionEntidad tipoIdentificacion) { this.tipoIdentificacion = UtilObjeto.obtenerValorDefecto(tipoIdentificacion, new TipoIdentificacionEntidad.Builder().build()); return this; }
        public Builder numeroIdentificacion(final String numeroIdentificacion) { this.numeroIdentificacion = UtilTexto.aplicarTrim(numeroIdentificacion); return this; }
        public Builder primerNombre(final String primerNombre) { this.primerNombre = UtilTexto.aplicarTrim(primerNombre); return this; }
        public Builder segundoNombre(final String segundoNombre) { this.segundoNombre = UtilTexto.aplicarTrim(segundoNombre); return this; }
        public Builder primerApellido(final String primerApellido) { this.primerApellido = UtilTexto.aplicarTrim(primerApellido); return this; }
        public Builder segundoApellido(final String segundoApellido) { this.segundoApellido = UtilTexto.aplicarTrim(segundoApellido); return this; }
        public Builder correoElectronico(final String correoElectronico) { this.correoElectronico = UtilTexto.aplicarTrim(correoElectronico); return this; }
        public UsuarioEntidad build() { return new UsuarioEntidad(this); }
    }
}
