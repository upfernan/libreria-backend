package com.libreria.negocio.dominio;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;

public class DevolucionDominio {

    private UUID id;
    private LocalDate fechaDevolucion;
    private PrestamoDominio prestamo;

    private DevolucionDominio(final Builder builder) {
        setId(builder.id);
        setFechaDevolucion(builder.fechaDevolucion);
        setPrestamo(builder.prestamo);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public PrestamoDominio getPrestamo() {
        return prestamo;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setFechaDevolucion(final LocalDate fechaDevolucion) {
        this.fechaDevolucion = UtilFecha.obtenerValorDefecto(fechaDevolucion);
    }

    private void setPrestamo(final PrestamoDominio prestamo) {
        this.prestamo = UtilObjeto.obtenerValorDefecto(prestamo, new PrestamoDominio.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LocalDate fechaDevolucion;
        private PrestamoDominio prestamo;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder fechaDevolucion(final LocalDate fechaDevolucion) {
            this.fechaDevolucion = UtilFecha.obtenerValorDefecto(fechaDevolucion);
            return this;
        }

        public Builder prestamo(final PrestamoDominio prestamo) {
            this.prestamo = UtilObjeto.obtenerValorDefecto(prestamo, new PrestamoDominio.Builder().build());
            return this;
        }

        public DevolucionDominio build() {
            return new DevolucionDominio(this);
        }
    }
}