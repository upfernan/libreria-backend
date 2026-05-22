package com.libreria.negocio.casouso.signatura.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.ActualizarSignaturaCasoUso;
import com.libreria.negocio.dominio.SignaturaDominio;
import com.libreria.transversal.utilitario.UtilCaracter;
import com.libreria.transversal.utilitario.UtilNumero;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class ActualizarSignaturaCasoUsoImpl implements ActualizarSignaturaCasoUso {

    private final DAOFactory daoFactory;

    public ActualizarSignaturaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final SignaturaDominio datos) {
        // P4 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P5 — Validar que la signatura exista en el sistema
        validarExistencia(datos.getId());
        // P2 — Validar que no exista otra signatura con la misma combinación excluyendo la actual
        validarCombinacionUnicaExcluyendo(datos.getPasillo(), datos.getEstante(), datos.getPosicion(), datos.getId());
       
        daoFactory.getSignaturaDAO().actualizar(datos.getId(), construirEntidad(datos));
    }

    // P4 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final SignaturaDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la signatura son obligatorios.", "Se recibió un objeto SignaturaDominio nulo.");
        }
        if (UtilUUID.esNulo(datos.getId())) {
            throw GestorLibreriaExcepcion.crear("El identificador de la signatura es obligatorio.", "El campo id llegó nulo en SignaturaDominio.");
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

    // P5 — Validar que la signatura exista en el sistema
    private void validarExistencia(final UUID id) {
        final SignaturaEntidad entidad = daoFactory.getSignaturaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La signatura indicada no existe en el sistema.", "No se encontró Signatura con id: " + id);
        }
    }

    // P2 — Validar que no exista otra signatura con la misma combinación excluyendo la actual
    private void validarCombinacionUnicaExcluyendo(final char pasillo, final Integer estante, final Integer posicion, final UUID id) {
        final SignaturaEntidad filtro = new SignaturaEntidad.Builder()
                .pasillo(pasillo)
                .estante(estante)
                .posicion(posicion)
                .build();
        final List<SignaturaEntidad> existentes = daoFactory.getSignaturaDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes)) {
            for (final SignaturaEntidad existente : existentes) {
                if (!existente.getId().equals(id)) {
                    throw GestorLibreriaExcepcion.crear("Ya existe otra signatura con esa ubicación.", "Combinación duplicada en signatura: pasillo=" + pasillo + ", estante=" + estante + ", posicion=" + posicion);
                }
            }
        }
    }

    private SignaturaEntidad construirEntidad(final SignaturaDominio datos) {
        return new SignaturaEntidad.Builder()
                .id(datos.getId())
                .pasillo(datos.getPasillo())
                .estante(datos.getEstante())
                .posicion(datos.getPosicion())
                .build();
    }
}
