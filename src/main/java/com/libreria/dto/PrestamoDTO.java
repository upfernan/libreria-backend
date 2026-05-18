package com.libreria.dto;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;

public class PrestamoDTO {

    private UUID id;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private EstadoPrestamoDTO estadoPrestamo;
    private ReservaDTO reserva;
    private UsuarioDTO usuario;
    private EjemplarDTO ejemplar;

    private PrestamoDTO(final Builder builder) {
        setId(builder.id);
        setFechaPrestamo(builder.fechaPrestamo);
        setFechaDevolucionEsperada(builder.fechaDevolucionEsperada);
        setEstadoPrestamo(builder.estadoPrestamo);
        setReserva(builder.reserva);
        setUsuario(builder.usuario);
        setEjemplar(builder.ejemplar);
    }

    public PrestamoDTO() {
        setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        setFechaPrestamo(null);
        setFechaDevolucionEsperada(null);
        setEstadoPrestamo(null);
        setReserva(null);
        setUsuario(null);
        setEjemplar(null);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }

    public EstadoPrestamoDTO getEstadoPrestamo() {
        return estadoPrestamo;
    }

    public ReservaDTO getReserva() {
        return reserva;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public EjemplarDTO getEjemplar() {
        return ejemplar;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setFechaPrestamo(final LocalDate fechaPrestamo) {
        this.fechaPrestamo = UtilFecha.obtenerValorDefecto(fechaPrestamo);
    }

    private void setFechaDevolucionEsperada(final LocalDate fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = UtilFecha.obtenerValorDefecto(fechaDevolucionEsperada);
    }

    private void setEstadoPrestamo(final EstadoPrestamoDTO estadoPrestamo) {
        this.estadoPrestamo = UtilObjeto.obtenerValorDefecto(estadoPrestamo, new EstadoPrestamoDTO.Builder().build());
    }

    private void setReserva(final ReservaDTO reserva) {
        this.reserva = UtilObjeto.obtenerValorDefecto(reserva, new ReservaDTO.Builder().build());
    }

    private void setUsuario(final UsuarioDTO usuario) {
        this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioDTO.Builder().build());
    }

    private void setEjemplar(final EjemplarDTO ejemplar) {
        this.ejemplar = UtilObjeto.obtenerValorDefecto(ejemplar, new EjemplarDTO.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LocalDate fechaPrestamo;
        private LocalDate fechaDevolucionEsperada;
        private EstadoPrestamoDTO estadoPrestamo;
        private ReservaDTO reserva;
        private UsuarioDTO usuario;
        private EjemplarDTO ejemplar;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder fechaPrestamo(final LocalDate fechaPrestamo) {
            this.fechaPrestamo = UtilFecha.obtenerValorDefecto(fechaPrestamo);
            return this;
        }

        public Builder fechaDevolucionEsperada(final LocalDate fechaDevolucionEsperada) {
            this.fechaDevolucionEsperada = UtilFecha.obtenerValorDefecto(fechaDevolucionEsperada);
            return this;
        }

        public Builder estadoPrestamo(final EstadoPrestamoDTO estadoPrestamo) {
            this.estadoPrestamo = UtilObjeto.obtenerValorDefecto(estadoPrestamo, new EstadoPrestamoDTO.Builder().build());
            return this;
        }

        public Builder reserva(final ReservaDTO reserva) {
            this.reserva = UtilObjeto.obtenerValorDefecto(reserva, new ReservaDTO.Builder().build());
            return this;
        }

        public Builder usuario(final UsuarioDTO usuario) {
            this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioDTO.Builder().build());
            return this;
        }

        public Builder ejemplar(final EjemplarDTO ejemplar) {
            this.ejemplar = UtilObjeto.obtenerValorDefecto(ejemplar, new EjemplarDTO.Builder().build());
            return this;
        }

        public PrestamoDTO build() {
            return new PrestamoDTO(this);
        }
    }
}
