package com.libreria.dto;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.UtilFecha;
import com.libreria.transversal.UtilObjeto;

public class ReservaDTO {

    private UUID id;
    private LocalDate fechaReserva;
    private LocalDate fechaExpiracion;
    private EstadoReservaDTO estadoReserva;
    private UsuarioDTO usuario;
    private LibroDTO libro;

    private ReservaDTO(final Builder builder) {
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

    public EstadoReservaDTO getEstadoReserva() {
        return estadoReserva;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public LibroDTO getLibro() {
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

    private void setEstadoReserva(final EstadoReservaDTO estadoReserva) {
        this.estadoReserva = UtilObjeto.obtenerValorDefecto(estadoReserva, new EstadoReservaDTO.Builder().build());
    }

    private void setUsuario(final UsuarioDTO usuario) {
        this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioDTO.Builder().build());
    }

    private void setLibro(final LibroDTO libro) {
        this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDTO.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LocalDate fechaReserva;
        private LocalDate fechaExpiracion;
        private EstadoReservaDTO estadoReserva;
        private UsuarioDTO usuario;
        private LibroDTO libro;

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

        public Builder estadoReserva(final EstadoReservaDTO estadoReserva) {
            this.estadoReserva = UtilObjeto.obtenerValorDefecto(estadoReserva, new EstadoReservaDTO.Builder().build());
            return this;
        }

        public Builder usuario(final UsuarioDTO usuario) {
            this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioDTO.Builder().build());
            return this;
        }

        public Builder libro(final LibroDTO libro) {
            this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDTO.Builder().build());
            return this;
        }

        public ReservaDTO build() {
            return new ReservaDTO(this);
        }
    }
}
