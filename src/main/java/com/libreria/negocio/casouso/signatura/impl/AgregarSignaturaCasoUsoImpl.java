package com.libreria.negocio.casouso.signatura.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.AgregarSignaturaCasoUso;
import com.libreria.negocio.dominio.SignaturaDominio;
import com.libreria.transversal.utilitario.UtilCaracter;
import com.libreria.transversal.utilitario.UtilNumero;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarSignaturaCasoUsoImpl implements AgregarSignaturaCasoUso {

    private final DAOFactory daoFactory;

    public AgregarSignaturaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final SignaturaDominio datos) {
        // P4 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P2 — Validar que no exista otra signatura con la misma combinación pasillo + estante + posición
        validarCombinacionUnica(datos.getPasillo(), datos.getEstante(), datos.getPosicion());
        // P1 — Registrar la signatura garantizando identificador único
        guardarNuevaSignatura(datos);
    }

    // P4 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final SignaturaDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la signatura son obligatorios.", "Se recibió un objeto SignaturaDominio nulo.");
        }
        if (UtilCaracter.esVacio(datos.getPasillo())) {
            throw GestorLibreriaExcepcion.crear("El pasillo de la signatura es obligatorio.", "El campo pasillo llegó vacío en SignaturaDominio.");
        }
        if (datos.getPasillo() < 'A' || datos.getPasillo() > 'Z') {
            throw GestorLibreriaExcepcion.crear("El pasillo de la signatura debe ser una letra mayúscula entre A y Z.", "pasillo con formato inválido en SignaturaDominio: " + datos.getPasillo());
        }
        if (UtilNumero.esNulo(datos.getEstante()) || datos.getEstante() <= 0) {
            throw GestorLibreriaExcepcion.crear("El estante de la signatura es obligatorio y debe ser mayor que cero.", "El campo estante llegó nulo o inválido en SignaturaDominio: " + datos.getEstante());
        }
        if (UtilNumero.esNulo(datos.getPosicion()) || datos.getPosicion() <= 0) {
            throw GestorLibreriaExcepcion.crear("La posición de la signatura es obligatoria y debe ser mayor que cero.", "El campo posicion llegó nulo o inválido en SignaturaDominio: " + datos.getPosicion());
        }
    }

    // P2 — Validar que no exista otra signatura con la misma combinación
    private void validarCombinacionUnica(final char pasillo, final Integer estante, final Integer posicion) {
        final SignaturaEntidad filtro = new SignaturaEntidad.Builder()
                .pasillo(pasillo)
                .estante(estante)
                .posicion(posicion)
                .build();
        final List<SignaturaEntidad> existentes = daoFactory.getSignaturaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("Ya existe una signatura con esa ubicación.", "Combinación duplicada en signatura: pasillo=" + pasillo + ", estante=" + estante + ", posicion=" + posicion);
        }
    }

    // P1 — Generar id único y persistir la nueva signatura
    private void guardarNuevaSignatura(final SignaturaDominio datos) {
        UUID id = UtilUUID.generar();
        while (!UtilObjeto.esNulo(daoFactory.getSignaturaDAO().consultarPorId(id))) {
            id = UtilUUID.generar();
        }
        final SignaturaEntidad nueva = new SignaturaEntidad.Builder()
                .id(id)
                .pasillo(datos.getPasillo())
                .estante(datos.getEstante())
                .posicion(datos.getPosicion())
                .build();
        daoFactory.getSignaturaDAO().crear(nueva);
    }
}
