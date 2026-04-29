package com.libreria.entidad;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.UtilFecha;
import com.libreria.transversal.UtilObjeto;

public class PagoEntidad {

    private UUID id;
    private LocalDate fechaPago;
    private MultaEntidad multa;

    private PagoEntidad(final Builder builder) {
        setId(builder.id);
        setFechaPago(builder.fechaPago);
        setMulta(builder.multa);
    }

    public UUID getId() { return id; }
    public LocalDate getFechaPago() { return fechaPago; }
    public MultaEntidad getMulta() { return multa; }

    private void setId(final UUID id) { this.id = id; }
    private void setFechaPago(final LocalDate fechaPago) { this.fechaPago = UtilFecha.obtenerValorDefecto(fechaPago); }
    private void setMulta(final MultaEntidad multa) { this.multa = UtilObjeto.obtenerValorDefecto(multa, new MultaEntidad.Builder().build()); }

    public static class Builder {
        private UUID id;
        private LocalDate fechaPago;
        private MultaEntidad multa;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder fechaPago(final LocalDate fechaPago) { this.fechaPago = UtilFecha.obtenerValorDefecto(fechaPago); return this; }
        public Builder multa(final MultaEntidad multa) { this.multa = UtilObjeto.obtenerValorDefecto(multa, new MultaEntidad.Builder().build()); return this; }
        public PagoEntidad build() { return new PagoEntidad(this); }
    }
}
