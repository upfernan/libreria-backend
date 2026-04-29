package com.libreria.negocio.dominio;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.UtilBooleano;
import com.libreria.transversal.UtilFecha;
import com.libreria.transversal.UtilNumero;
import com.libreria.transversal.UtilObjeto;

public class MultaDominio {

    private UUID id;
    private Double montoTotal;
    private LocalDate fechaGeneracion;
    private Boolean pagada;
    private Integer diasRetraso;
    private TarifaMultaDominio tarifaMulta;
    private DevolucionDominio devolucion;
    private UsuarioDominio usuarioAfectado;

    private MultaDominio(final Builder builder) {
        setId(builder.id);
        setMontoTotal(builder.montoTotal);
        setFechaGeneracion(builder.fechaGeneracion);
        setPagada(builder.pagada);
        setDiasRetraso(builder.diasRetraso);
        setTarifaMulta(builder.tarifaMulta);
        setDevolucion(builder.devolucion);
        setUsuarioAfectado(builder.usuarioAfectado);
    }

    public UUID getId() {
        return id;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public Boolean getPagada() {
        return pagada;
    }

    public Integer getDiasRetraso() {
        return diasRetraso;
    }

    public TarifaMultaDominio getTarifaMulta() {
        return tarifaMulta;
    }

    public DevolucionDominio getDevolucion() {
        return devolucion;
    }

    public UsuarioDominio getUsuarioAfectado() {
        return usuarioAfectado;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setMontoTotal(final Double montoTotal) {
        this.montoTotal = UtilNumero.obtenerValorDefecto(montoTotal, 0.0);
    }

    private void setFechaGeneracion(final LocalDate fechaGeneracion) {
        this.fechaGeneracion = UtilFecha.obtenerValorDefecto(fechaGeneracion);
    }

    private void setPagada(final Boolean pagada) {
        this.pagada = UtilBooleano.obtenerValorDefecto(pagada, false);
    }

    private void setDiasRetraso(final Integer diasRetraso) {
        this.diasRetraso = UtilNumero.obtenerValorDefecto(diasRetraso, 0);
    }

    private void setTarifaMulta(final TarifaMultaDominio tarifaMulta) {
        this.tarifaMulta = UtilObjeto.obtenerValorDefecto(tarifaMulta, new TarifaMultaDominio.Builder().build());
    }

    private void setDevolucion(final DevolucionDominio devolucion) {
        this.devolucion = UtilObjeto.obtenerValorDefecto(devolucion, new DevolucionDominio.Builder().build());
    }

    private void setUsuarioAfectado(final UsuarioDominio usuarioAfectado) {
        this.usuarioAfectado = UtilObjeto.obtenerValorDefecto(usuarioAfectado, new UsuarioDominio.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private Double montoTotal;
        private LocalDate fechaGeneracion;
        private Boolean pagada;
        private Integer diasRetraso;
        private TarifaMultaDominio tarifaMulta;
        private DevolucionDominio devolucion;
        private UsuarioDominio usuarioAfectado;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder montoTotal(final Double montoTotal) {
            this.montoTotal = UtilNumero.obtenerValorDefecto(montoTotal, 0.0);
            return this;
        }

        public Builder fechaGeneracion(final LocalDate fechaGeneracion) {
            this.fechaGeneracion = UtilFecha.obtenerValorDefecto(fechaGeneracion);
            return this;
        }

        public Builder pagada(final Boolean pagada) {
            this.pagada = UtilBooleano.obtenerValorDefecto(pagada, false);
            return this;
        }

        public Builder diasRetraso(final Integer diasRetraso) {
            this.diasRetraso = UtilNumero.obtenerValorDefecto(diasRetraso, 0);
            return this;
        }

        public Builder tarifaMulta(final TarifaMultaDominio tarifaMulta) {
            this.tarifaMulta = UtilObjeto.obtenerValorDefecto(tarifaMulta, new TarifaMultaDominio.Builder().build());
            return this;
        }

        public Builder devolucion(final DevolucionDominio devolucion) {
            this.devolucion = UtilObjeto.obtenerValorDefecto(devolucion, new DevolucionDominio.Builder().build());
            return this;
        }

        public Builder usuarioAfectado(final UsuarioDominio usuarioAfectado) {
            this.usuarioAfectado = UtilObjeto.obtenerValorDefecto(usuarioAfectado, new UsuarioDominio.Builder().build());
            return this;
        }

        public MultaDominio build() {
            return new MultaDominio(this);
        }
    }
}
