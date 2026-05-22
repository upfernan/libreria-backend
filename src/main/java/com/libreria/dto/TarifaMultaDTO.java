package com.libreria.dto;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilNumero;
import com.libreria.transversal.utilitario.UtilUUID;

public class TarifaMultaDTO {

    private UUID id;
    private Double valorDiario;
    private LocalDate fechaInicioVigencia;
    private LocalDate fechaFinVigencia;

    private TarifaMultaDTO(final Builder builder) {
        setId(builder.id);
        setValorDiario(builder.valorDiario);
        setFechaInicioVigencia(builder.fechaInicioVigencia);
        setFechaFinVigencia(builder.fechaFinVigencia);
    }

    public TarifaMultaDTO() {
        setId(UtilUUID.UUID_DEFECTO);
        setValorDiario(UtilNumero.DECIMAL_DEFECTO);
        setFechaInicioVigencia(UtilFecha.FECHA_DEFECTO);
        setFechaFinVigencia(UtilFecha.FECHA_DEFECTO);
    }

    public UUID getId() {
        return id;
    }

    public Double getValorDiario() {
        return valorDiario;
    }

    public LocalDate getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public LocalDate getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    private void setId(final UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    private void setValorDiario(final Double valorDiario) {
        this.valorDiario = UtilNumero.obtenerValorDefecto(valorDiario, 0.0);
    }

    private void setFechaInicioVigencia(final LocalDate fechaInicioVigencia) {
        this.fechaInicioVigencia = UtilFecha.obtenerValorDefecto(fechaInicioVigencia);
    }

    private void setFechaFinVigencia(final LocalDate fechaFinVigencia) {
        this.fechaFinVigencia = UtilFecha.obtenerValorDefecto(fechaFinVigencia);
    }

    public static class Builder {
        private UUID id;
        private Double valorDiario;
        private LocalDate fechaInicioVigencia;
        private LocalDate fechaFinVigencia;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder valorDiario(final Double valorDiario) {
            this.valorDiario = UtilNumero.obtenerValorDefecto(valorDiario, 0.0);
            return this;
        }

        public Builder fechaInicioVigencia(final LocalDate fechaInicioVigencia) {
            this.fechaInicioVigencia = UtilFecha.obtenerValorDefecto(fechaInicioVigencia);
            return this;
        }

        public Builder fechaFinVigencia(final LocalDate fechaFinVigencia) {
            this.fechaFinVigencia = UtilFecha.obtenerValorDefecto(fechaFinVigencia);
            return this;
        }

        public TarifaMultaDTO build() {
            return new TarifaMultaDTO(this);
        }
    }
}
