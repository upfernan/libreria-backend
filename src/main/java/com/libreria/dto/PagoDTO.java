package com.libreria.dto;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;

public class PagoDTO {

    private UUID id;
    private LocalDate fechaPago;
    private MultaDTO multa;

    private PagoDTO(final Builder builder) {
        setId(builder.id);
        setFechaPago(builder.fechaPago);
        setMulta(builder.multa);
    }

    public PagoDTO() {
        setId(UtilUUID.UUID_DEFECTO);
        setFechaPago(UtilFecha.FECHA_DEFECTO);
        setMulta(new MultaDTO.Builder().build());
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public MultaDTO getMulta() {
        return multa;
    }

    private void setId(final UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    private void setFechaPago(final LocalDate fechaPago) {
        this.fechaPago = UtilFecha.obtenerValorDefecto(fechaPago);
    }

    private void setMulta(final MultaDTO multa) {
        this.multa = UtilObjeto.obtenerValorDefecto(multa, new MultaDTO.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LocalDate fechaPago;
        private MultaDTO multa;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder fechaPago(final LocalDate fechaPago) {
            this.fechaPago = UtilFecha.obtenerValorDefecto(fechaPago);
            return this;
        }

        public Builder multa(final MultaDTO multa) {
            this.multa = UtilObjeto.obtenerValorDefecto(multa, new MultaDTO.Builder().build());
            return this;
        }

        public PagoDTO build() {
            return new PagoDTO(this);
        }
    }
}
