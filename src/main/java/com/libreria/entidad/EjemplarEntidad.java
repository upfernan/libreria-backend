package com.libreria.entidad;

import java.util.UUID;

import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;

public class EjemplarEntidad {

    private UUID id;
    private LibroEntidad libro;
    private SignaturaEntidad signatura;

    private EjemplarEntidad(final Builder builder) {
        setId(builder.id);
        setLibro(builder.libro);
        setSignatura(builder.signatura);
    }

    public UUID getId() { return id; }
    public LibroEntidad getLibro() { return libro; }
    public SignaturaEntidad getSignatura() { return signatura; }

    private void setId(final UUID id) { this.id = id; }
    private void setLibro(final LibroEntidad libro) { this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroEntidad.Builder().build()); }
    private void setSignatura(final SignaturaEntidad signatura) { this.signatura = UtilObjeto.obtenerValorDefecto(signatura, new SignaturaEntidad.Builder().build()); }


    public boolean isIdValorPorDefecto() {
        return UtilUUID.esValorDefecto(id);
    }
    public static class Builder {
        private UUID id;
        private LibroEntidad libro;
        private SignaturaEntidad signatura;

        public Builder id(final UUID id) { this.id = id; return this; }
        public Builder libro(final LibroEntidad libro) { this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroEntidad.Builder().build()); return this; }
        public Builder signatura(final SignaturaEntidad signatura) { this.signatura = UtilObjeto.obtenerValorDefecto(signatura, new SignaturaEntidad.Builder().build()); return this; }
        public EjemplarEntidad build() { return new EjemplarEntidad(this); }
    }
}
