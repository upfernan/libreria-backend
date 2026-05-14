package com.libreria.negocio.dominio;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilObjeto;

public class PagoDominio {

    private UUID id;
    private LocalDate fechaPago;
    private MultaDominio multa;

    private PagoDominio(final Builder builder) {
        setId(builder.id);
        setFechaPago(builder.fechaPago);
        setMulta(builder.multa);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public MultaDominio getMulta() {
        return multa;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setFechaPago(final LocalDate fechaPago) {
        this.fechaPago = UtilFecha.obtenerValorDefecto(fechaPago);
    }

    private void setMulta(final MultaDominio multa) {
        this.multa = UtilObjeto.obtenerValorDefecto(multa, new MultaDominio.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LocalDate fechaPago;
        private MultaDominio multa;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder fechaPago(final LocalDate fechaPago) {
            this.fechaPago = UtilFecha.obtenerValorDefecto(fechaPago);
            return this;
        }

        public Builder multa(final MultaDominio multa) {
            this.multa = UtilObjeto.obtenerValorDefecto(multa, new MultaDominio.Builder().build());
            return this;
        }

        public PagoDominio build() {
            return new PagoDominio(this);
        }
    }
}
