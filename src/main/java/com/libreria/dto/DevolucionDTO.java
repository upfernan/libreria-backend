package com.libreria.dto;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;

public class DevolucionDTO {

    private UUID id;
    private LocalDate fechaDevolucion;
    private PrestamoDTO prestamo;

    private DevolucionDTO(final Builder builder) {
        setId(builder.id);
        setFechaDevolucion(builder.fechaDevolucion);
        setPrestamo(builder.prestamo);
    }

    public DevolucionDTO() {
        setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        setFechaDevolucion(null);
        setPrestamo(null);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public PrestamoDTO getPrestamo() {
        return prestamo;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setFechaDevolucion(final LocalDate fechaDevolucion) {
        this.fechaDevolucion = UtilFecha.obtenerValorDefecto(fechaDevolucion);
    }

    private void setPrestamo(final PrestamoDTO prestamo) {
        this.prestamo = UtilObjeto.obtenerValorDefecto(prestamo, new PrestamoDTO.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LocalDate fechaDevolucion;
        private PrestamoDTO prestamo;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder fechaDevolucion(final LocalDate fechaDevolucion) {
            this.fechaDevolucion = UtilFecha.obtenerValorDefecto(fechaDevolucion);
            return this;
        }

        public Builder prestamo(final PrestamoDTO prestamo) {
            this.prestamo = UtilObjeto.obtenerValorDefecto(prestamo, new PrestamoDTO.Builder().build());
            return this;
        }

        public DevolucionDTO build() {
            return new DevolucionDTO(this);
        }
    }
}
