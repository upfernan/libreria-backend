package com.libreria.negocio.casouso.signatura.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EjemplarEntidad;
import com.libreria.entidad.SignaturaEntidad;
import com.libreria.negocio.casouso.signatura.RetirarSignaturaCasoUso;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class RetirarSignaturaCasoUsoImpl implements RetirarSignaturaCasoUso {

    private final DAOFactory daoFactory;

    public RetirarSignaturaCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final UUID id) {
        // P5 — Validar que la signatura exista en el sistema
        validarExistencia(id);
        // P6 — Validar que la signatura no esté en uso por ningún ejemplar
        validarNoEnUso(id);
        
        daoFactory.getSignaturaDAO().eliminar(id);
    }

    // P5 — Validar que la signatura exista en el sistema
    private void validarExistencia(final UUID id) {
        final SignaturaEntidad entidad = daoFactory.getSignaturaDAO().consultarPorId(id);
        if (UtilObjeto.esNulo(entidad) || UtilObjeto.esNulo(entidad.getId())) {
            throw GestorLibreriaExcepcion.crear("La signatura indicada no existe en el sistema.", "No se encontró Signatura con id: " + id);
        }
    }

    // P6 — Validar que la signatura no esté siendo utilizada por ningún ejemplar
    private void validarNoEnUso(final UUID id) {
        final EjemplarEntidad filtro = new EjemplarEntidad.Builder()
                .signatura(new SignaturaEntidad.Builder().id(id).build())
                .build();
        final List<EjemplarEntidad> ejemplares = daoFactory.getEjemplarDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(ejemplares) && !ejemplares.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("La signatura está en uso y no puede eliminarse.", "signaturaId: " + id);
        }
    }
}
