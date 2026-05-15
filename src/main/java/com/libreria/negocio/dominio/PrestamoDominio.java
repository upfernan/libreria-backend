package com.libreria.negocio.dominio;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;

public class PrestamoDominio {

    private UUID id;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private EstadoPrestamoDominio estadoPrestamo;
    private ReservaDominio reserva;
    private UsuarioDominio usuario;
    private EjemplarDominio ejemplar;

    private PrestamoDominio(final Builder builder) {
        setId(builder.id);
        setFechaPrestamo(builder.fechaPrestamo);
        setFechaDevolucionEsperada(builder.fechaDevolucionEsperada);
        setEstadoPrestamo(builder.estadoPrestamo);
        setReserva(builder.reserva);
        setUsuario(builder.usuario);
        setEjemplar(builder.ejemplar);
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

    public EstadoPrestamoDominio getEstadoPrestamo() {
        return estadoPrestamo;
    }

    public ReservaDominio getReserva() {
        return reserva;
    }

    public UsuarioDominio getUsuario() {
        return usuario;
    }

    public EjemplarDominio getEjemplar() {
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

    private void setEstadoPrestamo(final EstadoPrestamoDominio estadoPrestamo) {
        this.estadoPrestamo = UtilObjeto.obtenerValorDefecto(estadoPrestamo, new EstadoPrestamoDominio.Builder().build());
    }

    private void setReserva(final ReservaDominio reserva) {
        this.reserva = UtilObjeto.obtenerValorDefecto(reserva, new ReservaDominio.Builder().build());
    }

    private void setUsuario(final UsuarioDominio usuario) {
        this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioDominio.Builder().build());
    }

    private void setEjemplar(final EjemplarDominio ejemplar) {
        this.ejemplar = UtilObjeto.obtenerValorDefecto(ejemplar, new EjemplarDominio.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LocalDate fechaPrestamo;
        private LocalDate fechaDevolucionEsperada;
        private EstadoPrestamoDominio estadoPrestamo;
        private ReservaDominio reserva;
        private UsuarioDominio usuario;
        private EjemplarDominio ejemplar;

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

        public Builder estadoPrestamo(final EstadoPrestamoDominio estadoPrestamo) {
            this.estadoPrestamo = UtilObjeto.obtenerValorDefecto(estadoPrestamo, new EstadoPrestamoDominio.Builder().build());
            return this;
        }

        public Builder reserva(final ReservaDominio reserva) {
            this.reserva = UtilObjeto.obtenerValorDefecto(reserva, new ReservaDominio.Builder().build());
            return this;
        }

        public Builder usuario(final UsuarioDominio usuario) {
            this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder ejemplar(final EjemplarDominio ejemplar) {
            this.ejemplar = UtilObjeto.obtenerValorDefecto(ejemplar, new EjemplarDominio.Builder().build());
            return this;
        }

        public PrestamoDominio build() {
            return new PrestamoDominio(this);
        }
    }
}
