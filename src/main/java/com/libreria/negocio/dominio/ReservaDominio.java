package com.libreria.negocio.dominio;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;

public class ReservaDominio {

    private UUID id;
    private LocalDate fechaReserva;
    private LocalDate fechaExpiracion;
    private EstadoReservaDominio estadoReserva;
    private UsuarioDominio usuario;
    private LibroDominio libro;

    private ReservaDominio(final Builder builder) {
        setId(builder.id);
        setFechaReserva(builder.fechaReserva);
        setFechaExpiracion(builder.fechaExpiracion);
        setEstadoReserva(builder.estadoReserva);
        setUsuario(builder.usuario);
        setLibro(builder.libro);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public EstadoReservaDominio getEstadoReserva() {
        return estadoReserva;
    }

    public UsuarioDominio getUsuario() {
        return usuario;
    }

    public LibroDominio getLibro() {
        return libro;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setFechaReserva(final LocalDate fechaReserva) {
        this.fechaReserva = UtilFecha.obtenerValorDefecto(fechaReserva);
    }

    private void setFechaExpiracion(final LocalDate fechaExpiracion) {
        this.fechaExpiracion = UtilFecha.obtenerValorDefecto(fechaExpiracion);
    }

    private void setEstadoReserva(final EstadoReservaDominio estadoReserva) {
        this.estadoReserva = UtilObjeto.obtenerValorDefecto(estadoReserva, new EstadoReservaDominio.Builder().build());
    }

    private void setUsuario(final UsuarioDominio usuario) {
        this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioDominio.Builder().build());
    }

    private void setLibro(final LibroDominio libro) {
        this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDominio.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LocalDate fechaReserva;
        private LocalDate fechaExpiracion;
        private EstadoReservaDominio estadoReserva;
        private UsuarioDominio usuario;
        private LibroDominio libro;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder fechaReserva(final LocalDate fechaReserva) {
            this.fechaReserva = UtilFecha.obtenerValorDefecto(fechaReserva);
            return this;
        }

        public Builder fechaExpiracion(final LocalDate fechaExpiracion) {
            this.fechaExpiracion = UtilFecha.obtenerValorDefecto(fechaExpiracion);
            return this;
        }

        public Builder estadoReserva(final EstadoReservaDominio estadoReserva) {
            this.estadoReserva = UtilObjeto.obtenerValorDefecto(estadoReserva, new EstadoReservaDominio.Builder().build());
            return this;
        }

        public Builder usuario(final UsuarioDominio usuario) {
            this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder libro(final LibroDominio libro) {
            this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDominio.Builder().build());
            return this;
        }

        public ReservaDominio build() {
            return new ReservaDominio(this);
        }
    }
}