package com.libreria.entidad;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;

public class PrestamoEntidad {

    private UUID id;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private EstadoPrestamoEntidad estadoPrestamo;
    private ReservaEntidad reserva;
    private UsuarioEntidad usuario;
    private EjemplarEntidad ejemplar;

    private PrestamoEntidad(final Builder builder) {
        setId(builder.id);
        setFechaPrestamo(builder.fechaPrestamo);
        setFechaDevolucionEsperada(builder.fechaDevolucionEsperada);
        setEstadoPrestamo(builder.estadoPrestamo);
        setReserva(builder.reserva);
        setUsuario(builder.usuario);
        setEjemplar(builder.ejemplar);
    }

    public UUID getId() { return id; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public EstadoPrestamoEntidad getEstadoPrestamo() { return estadoPrestamo; }
    public ReservaEntidad getReserva() { return reserva; }
    public UsuarioEntidad getUsuario() { return usuario; }
    public EjemplarEntidad getEjemplar() { return ejemplar; }

    private void setId(final UUID id) { this.id = id; }
    private void setFechaPrestamo(final LocalDate fechaPrestamo) { this.fechaPrestamo = UtilFecha.obtenerValorDefecto(fechaPrestamo); }
    private void setFechaDevolucionEsperada(final LocalDate fechaDevolucionEsperada) { this.fechaDevolucionEsperada = UtilFecha.obtenerValorDefecto(fechaDevolucionEsperada); }
    private void setEstadoPrestamo(final EstadoPrestamoEntidad estadoPrestamo) { this.estadoPrestamo = UtilObjeto.obtenerValorDefecto(estadoPrestamo, new EstadoPrestamoEntidad.Builder().build()); }
    private void setReserva(final ReservaEntidad reserva) { this.reserva = UtilObjeto.obtenerValorDefecto(reserva, new ReservaEntidad.Builder().build()); }
    private void setUsuario(final UsuarioEntidad usuario) { this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioEntidad.Builder().build()); }
    private void setEjemplar(final EjemplarEntidad ejemplar) { this.ejemplar = UtilObjeto.obtenerValorDefecto(ejemplar, new EjemplarEntidad.Builder().build()); }

    public static class Builder {
        private UUID id;
        private LocalDate fechaPrestamo;
        private LocalDate fechaDevolucionEsperada;
        private EstadoPrestamoEntidad estadoPrestamo;
        private ReservaEntidad reserva;
        private UsuarioEntidad usuario;
        private EjemplarEntidad ejemplar;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder fechaPrestamo(final LocalDate fechaPrestamo) { this.fechaPrestamo = UtilFecha.obtenerValorDefecto(fechaPrestamo); return this; }
        public Builder fechaDevolucionEsperada(final LocalDate fechaDevolucionEsperada) { this.fechaDevolucionEsperada = UtilFecha.obtenerValorDefecto(fechaDevolucionEsperada); return this; }
        public Builder estadoPrestamo(final EstadoPrestamoEntidad estadoPrestamo) { this.estadoPrestamo = UtilObjeto.obtenerValorDefecto(estadoPrestamo, new EstadoPrestamoEntidad.Builder().build()); return this; }
        public Builder reserva(final ReservaEntidad reserva) { this.reserva = UtilObjeto.obtenerValorDefecto(reserva, new ReservaEntidad.Builder().build()); return this; }
        public Builder usuario(final UsuarioEntidad usuario) { this.usuario = UtilObjeto.obtenerValorDefecto(usuario, new UsuarioEntidad.Builder().build()); return this; }
        public Builder ejemplar(final EjemplarEntidad ejemplar) { this.ejemplar = UtilObjeto.obtenerValorDefecto(ejemplar, new EjemplarEntidad.Builder().build()); return this; }
        public PrestamoEntidad build() { return new PrestamoEntidad(this); }
    }
}
