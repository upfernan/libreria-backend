package com.libreria.dto;

import java.util.UUID;
import com.libreria.transversal.UtilObjeto;

public class EjemplarDTO {

    private UUID id;
    private LibroDTO libro;
    private SignaturaDTO signatura;

    private EjemplarDTO(final Builder builder) {
        setId(builder.id);
        setLibro(builder.libro);
        setSignatura(builder.signatura);
    }

    public UUID getId() {
        return id;
    }

    public LibroDTO getLibro() {
        return libro;
    }

    public SignaturaDTO getSignatura() {
        return signatura;
    }

    private void setId(final UUID id) {
        this.id = id;
    }

    private void setLibro(final LibroDTO libro) {
        this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDTO.Builder().build());
    }

    private void setSignatura(final SignaturaDTO signatura) {
        this.signatura = UtilObjeto.obtenerValorDefecto(signatura, new SignaturaDTO.Builder().build());
    }

    public static class Builder {
        private UUID id;
        private LibroDTO libro;
        private SignaturaDTO signatura;

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder libro(final LibroDTO libro) {
            this.libro = UtilObjeto.obtenerValorDefecto(libro, new LibroDTO.Builder().build());
            return this;
        }

        public Builder signatura(final SignaturaDTO signatura) {
            this.signatura = UtilObjeto.obtenerValorDefecto(signatura, new SignaturaDTO.Builder().build());
            return this;
        }

        public EjemplarDTO build() {
            return new EjemplarDTO(this);
        }
    }
}
