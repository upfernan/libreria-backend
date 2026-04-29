package com.libreria.entidad;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.UtilFecha;
import com.libreria.transversal.UtilObjeto;

public class ReservaEntidad {

    private UUID id;
    private LocalDate fechaReserva;
    private LocalDate fechaExpiracion;
    private EstadoReservaEntidad estadoReserva;
    private UsuarioEntidad usuario;
    private LibroEntidad libro;

    private ReservaEntidad(final Builder builder) {
        setId(builder.id);
        setFechaReserva(builder.fechaReserva);
        setFechaExpiracion(builder.fechaExpiracion);
        setEstadoReserva(builder.estadoReserva);
        setUsuario(builder.usuario);
        setLibro(builder.libro);
    }

    public UUID getId() { return id; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
    public EstadoReservaEntidad getEstadoReserva() { return estadoReserva; }
    public UsuarioEntidad getUsuario() { return usuario; }
    public LibroEntidad getLibro() { return libro; }

    private void setId(final UUID id) { this.id = id; }
    private void setFechaReserva(final LocalDate fechaReserva) { this.fechaReserva = UtilFecha.obtenerValorDefecto(fechaReserva); }
    private void setFechaExpiracion(final LocalDate fechaExpiracion) { this.fechaExpiracion = UtilFecha.obtenerValorDefecto(fechaExpiracion); }
    private void setEstadoReserva(final EstadoReservaEntidad estadoReserva) { this.estadoReserva = UtilObjeto.obtenerValorDefecto(estadoReserva, new EstadoReservaEntidad.Builder().build()); }
    private void setUsuario(final UsuarioEntidad usuario) { this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioEntidad.Builder().build()); }
    private void setLibro(final LibroEntidad libro) { this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroEntidad.Builder().build()); }

    public static class Builder {
        private UUID id;
        private LocalDate fechaReserva;
        private LocalDate fechaExpiracion;
        private EstadoReservaEntidad estadoReserva;
        private UsuarioEntidad usuario;
        private LibroEntidad libro;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder fechaReserva(final LocalDate fechaReserva) { this.fechaReserva = UtilFecha.obtenerValorDefecto(fechaReserva); return this; }
        public Builder fechaExpiracion(final LocalDate fechaExpiracion) { this.fechaExpiracion = UtilFecha.obtenerValorDefecto(fechaExpiracion); return this; }
        public Builder estadoReserva(final EstadoReservaEntidad estadoReserva) { this.estadoReserva = UtilObjeto.obtenerValorDefecto(estadoReserva, new EstadoReservaEntidad.Builder().build()); return this; }
        public Builder usuario(final UsuarioEntidad usuario) { this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioEntidad.Builder().build()); return this; }
        public Builder libro(final LibroEntidad libro) { this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroEntidad.Builder().build()); return this; }
        public ReservaEntidad build() { return new ReservaEntidad(this); }
    }
}
