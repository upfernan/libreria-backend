package com.libreria.entidad;

import java.time.LocalDate;
import java.util.UUID;
import com.libreria.transversal.utilitario.UtilBooleano;
import com.libreria.transversal.utilitario.UtilFecha;
import com.libreria.transversal.utilitario.UtilNumero;
import com.libreria.transversal.utilitario.UtilObjeto;

public class MultaEntidad {

    private UUID id;
    private Double montoTotal;
    private LocalDate fechaGeneracion;
    private Boolean pagada;
    private Integer diasRetraso;
    private TarifaMultaEntidad tarifaMulta;
    private DevolucionEntidad devolucion;
    private UsuarioEntidad usuarioAfectado;

    private MultaEntidad(final Builder builder) {
        setId(builder.id);
        setMontoTotal(builder.montoTotal);
        setFechaGeneracion(builder.fechaGeneracion);
        setPagada(builder.pagada);
        setDiasRetraso(builder.diasRetraso);
        setTarifaMulta(builder.tarifaMulta);
        setDevolucion(builder.devolucion);
        setUsuarioAfectado(builder.usuarioAfectado);
    }

    public UUID getId() { return id; }
    public Double getMontoTotal() { return montoTotal; }
    public LocalDate getFechaGeneracion() { return fechaGeneracion; }
    public Boolean getPagada() { return pagada; }
    public Integer getDiasRetraso() { return diasRetraso; }
    public TarifaMultaEntidad getTarifaMulta() { return tarifaMulta; }
    public DevolucionEntidad getDevolucion() { return devolucion; }
    public UsuarioEntidad getUsuarioAfectado() { return usuarioAfectado; }

    private void setId(final UUID id) { this.id = id; }
    private void setMontoTotal(final Double montoTotal) { this.montoTotal = UtilNumero.obtenerValorDefecto(montoTotal, 0.0); }
    private void setFechaGeneracion(final LocalDate fechaGeneracion) { this.fechaGeneracion = UtilFecha.obtenerValorDefecto(fechaGeneracion); }
    private void setPagada(final Boolean pagada) { this.pagada = UtilBooleano.obtenerValorDefecto(pagada, false); }
    private void setDiasRetraso(final Integer diasRetraso) { this.diasRetraso = UtilNumero.obtenerValorDefecto(diasRetraso, 0); }
    private void setTarifaMulta(final TarifaMultaEntidad tarifaMulta) { this.tarifaMulta = UtilObjeto.obtenerValorDefecto(tarifaMulta, new TarifaMultaEntidad.Builder().build()); }
    private void setDevolucion(final DevolucionEntidad devolucion) { this.devolucion = UtilObjeto.obtenerValorDefecto(devolucion, new DevolucionEntidad.Builder().build()); }
    private void setUsuarioAfectado(final UsuarioEntidad usuarioAfectado) { this.usuarioAfectado = UtilObjeto.obtenerValorDefecto(usuarioAfectado, new UsuarioEntidad.Builder().build()); }

    public static class Builder {
        private UUID id;
        private Double montoTotal;
        private LocalDate fechaGeneracion;
        private Boolean pagada;
        private Integer diasRetraso;
        private TarifaMultaEntidad tarifaMulta;
        private DevolucionEntidad devolucion;
        private UsuarioEntidad usuarioAfectado;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder montoTotal(final Double montoTotal) { this.montoTotal = UtilNumero.obtenerValorDefecto(montoTotal, 0.0); return this; }
        public Builder fechaGeneracion(final LocalDate fechaGeneracion) { this.fechaGeneracion = UtilFecha.obtenerValorDefecto(fechaGeneracion); return this; }
        public Builder pagada(final Boolean pagada) { this.pagada = UtilBooleano.obtenerValorDefecto(pagada, false); return this; }
        public Builder diasRetraso(final Integer diasRetraso) { this.diasRetraso = UtilNumero.obtenerValorDefecto(diasRetraso, 0); return this; }
        public Builder tarifaMulta(final TarifaMultaEntidad tarifaMulta) { this.tarifaMulta = UtilObjeto.obtenerValorDefecto(tarifaMulta, new TarifaMultaEntidad.Builder().build()); return this; }
        public Builder devolucion(final DevolucionEntidad devolucion) { this.devolucion = UtilObjeto.obtenerValorDefecto(devolucion, new DevolucionEntidad.Builder().build()); return this; }
        public Builder usuarioAfectado(final UsuarioEntidad usuarioAfectado) { this.usuarioAfectado = UtilObjeto.obtenerValorDefecto(usuarioAfectado, new UsuarioEntidad.Builder().build()); return this; }
        public MultaEntidad build() { return new MultaEntidad(this); }
    }
}
