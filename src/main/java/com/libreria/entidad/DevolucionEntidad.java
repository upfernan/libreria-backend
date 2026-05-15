package com.libreria.entidad;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;

public class DevolucionEntidad {

    private UUID id;
    private LocalDate fechaDevolucion;
    private PrestamoEntidad prestamo;

    private DevolucionEntidad(final Builder builder) {
        setId(builder.id);
        setFechaDevolucion(builder.fechaDevolucion);
        setPrestamo(builder.prestamo);
    }

    public UUID getId() { return id; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public PrestamoEntidad getPrestamo() { return prestamo; }

    private void setId(final UUID id) { this.id = id; }
    private void setFechaDevolucion(final LocalDate fechaDevolucion) { this.fechaDevolucion = UtilFecha.obtenerValorDefecto(fechaDevolucion); }
    private void setPrestamo(final PrestamoEntidad prestamo) { this.prestamo = UtilObjeto.obtenerValorDefecto(prestamo, new PrestamoEntidad.Builder().build()); }

    public static class Builder {
        private UUID id;
        private LocalDate fechaDevolucion;
        private PrestamoEntidad prestamo;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder fechaDevolucion(final LocalDate fechaDevolucion) { this.fechaDevolucion = UtilFecha.obtenerValorDefecto(fechaDevolucion); return this; }
        public Builder prestamo(final PrestamoEntidad prestamo) { this.prestamo = UtilObjeto.obtenerValorDefecto(prestamo, new PrestamoEntidad.Builder().build()); return this; }
        public DevolucionEntidad build() { return new DevolucionEntidad(this); }
    }
}
