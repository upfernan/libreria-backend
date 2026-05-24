package com.libreria.negocio.dominio;

import java.util.UUID;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;

public class EjemplarDominio {

    private UUID id;
    private LibroDominio libro;
    private SignaturaDominio signatura;

    private EjemplarDominio(final Builder builder) {
        setId(builder.id);
        setLibro(builder.libro);
        setSignatura(builder.signatura);
    }

    public UUID getId() {
        return id;
    }

    public LibroDominio getLibro() {
        return libro;
    }

    public SignaturaDominio getSignatura() {
        return signatura;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setLibro(final LibroDominio libro) {
        this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDominio.Builder().build());
    }

    private void setSignatura(final SignaturaDominio signatura) {
        this.signatura = UtilObjeto.obtenerValorDefecto(signatura, new SignaturaDominio.Builder().build());
    }


    public boolean isIdValorPorDefecto() {
        return UtilUUID.esValorDefecto(id);
    }
    public static class Builder {
        private UUID id;
        private LibroDominio libro;
        private SignaturaDominio signatura;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder libro(final LibroDominio libro) {
            this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDominio.Builder().build());
            return this;
        }

        public Builder signatura(final SignaturaDominio signatura) {
            this.signatura = UtilObjeto.obtenerValorDefecto(signatura, new SignaturaDominio.Builder().build());
            return this;
        }

        public EjemplarDominio build() {
            return new EjemplarDominio(this);
        }
    }
}
