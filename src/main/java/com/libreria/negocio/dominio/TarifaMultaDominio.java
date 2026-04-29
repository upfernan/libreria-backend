package com.libreria.negocio.dominio;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.UtilFecha;
import com.libreria.transversal.UtilNumero;

public class TarifaMultaDominio {

    private UUID id;
    private Double valorDiario;
    private LocalDate fechaVigencia;

    private TarifaMultaDominio(final Builder builder) {
        setId(builder.id);
        setValorDiario(builder.valorDiario);
        setFechaVigencia(builder.fechaVigencia);
    }

    public UUID getId() {
        return id;
    }

    public Double getValorDiario() {
        return valorDiario;
    }

    public LocalDate getFechaVigencia() {
        return fechaVigencia;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setValorDiario(final Double valorDiario) {
        this.valorDiario = UtilNumero.obtenerValorDefecto(valorDiario, 0.0);
    }

    private void setFechaVigencia(final LocalDate fechaVigencia) {
        this.fechaVigencia = UtilFecha.obtenerValorDefecto(fechaVigencia);
    }

    public static class Builder {
        private UUID id;
        private Double valorDiario;
        private LocalDate fechaVigencia;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder valorDiario(final Double valorDiario) {
            this.valorDiario = UtilNumero.obtenerValorDefecto(valorDiario, 0.0);
            return this;
        }

        public Builder fechaVigencia(final LocalDate fechaVigencia) {
            this.fechaVigencia = UtilFecha.obtenerValorDefecto(fechaVigencia);
            return this;
        }

        public TarifaMultaDominio build() {
            return new TarifaMultaDominio(this);
        }
    }
}
