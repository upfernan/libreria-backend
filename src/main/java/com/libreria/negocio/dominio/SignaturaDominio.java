package com.libreria.negocio.dominio;

import java.util.UUID;
import com.libreria.transversal.utilitario.UtilCaracter;
import com.libreria.transversal.utilitario.UtilNumero;

public class SignaturaDominio {

    private UUID id;
    private char pasillo;
    private Integer estante;
    private Integer posicion;

    private SignaturaDominio(final Builder builder) {
        setId(builder.id);
        setPasillo(builder.pasillo);
        setEstante(builder.estante);
        setPosicion(builder.posicion);
    }

    public UUID getId() {
        return id;
    }

    public char getPasillo() {
        return pasillo;
    }

    public Integer getEstante() {
        return estante;
    }

    public Integer getPosicion() {
        return posicion;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setPasillo(final char pasillo) {
        this.pasillo = UtilCaracter.obtenerValorDefecto(pasillo);
    }

    private void setEstante(final Integer estante) {
        this.estante = UtilNumero.obtenerValorDefecto(estante, 0);
    }

    private void setPosicion(final Integer posicion) {
        this.posicion = UtilNumero.obtenerValorDefecto(posicion, 0);
    }

    public static class Builder {
        private UUID id;
        private char pasillo;
        private Integer estante;
        private Integer posicion;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder pasillo(final char pasillo) {
            this.pasillo = UtilCaracter.obtenerValorDefecto(pasillo);
            return this;
        }

        public Builder estante(final Integer estante) {
            this.estante = UtilNumero.obtenerValorDefecto(estante, 0);
            return this;
        }

        public Builder posicion(final Integer posicion) {
            this.posicion = UtilNumero.obtenerValorDefecto(posicion, 0);
            return this;
        }

        public SignaturaDominio build() {
            return new SignaturaDominio(this);
        }
    }
}
