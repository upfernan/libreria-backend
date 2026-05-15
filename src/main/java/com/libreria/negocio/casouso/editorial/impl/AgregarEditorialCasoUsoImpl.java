package com.libreria.negocio.casouso.editorial.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.EditorialEntidad;
import com.libreria.negocio.casouso.editorial.AgregarEditorialCasoUso;
import com.libreria.negocio.dominio.EditorialDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarEditorialCasoUsoImpl implements AgregarEditorialCasoUso {

    private final DAOFactory daoFactory;

    public AgregarEditorialCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final EditorialDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P2 — Validar que no exista otra editorial con el mismo nombre
        validarNombreUnico(datos.getNombre());
        // P1 — Registrar la editorial garantizando identificador único
        guardarNuevaEditorial(datos);
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final EditorialDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos de la editorial son obligatorios.", "Se recibió un objeto EditorialDominio nulo.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre de la editorial es obligatorio.", "El campo nombre llegó nulo o vacío en EditorialDominio.");
        }
        if (!UtilTexto.soloLetrasYEspacios(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre de la editorial solo puede contener letras y espacios.", "nombre con formato inválido en EditorialDominio: " + datos.getNombre());
        }
    }

    // P2 — Validar que no exista otra editorial con el mismo nombre
    private void validarNombreUnico(final String nombre) {
        final EditorialEntidad filtro = new EditorialEntidad.Builder().nombre(nombre).build();
        final List<EditorialEntidad> existentes = daoFactory.getEditorialDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("Ya existe una editorial con ese nombre.", "nombre duplicado en editorial: " + nombre);
        }
    }

    // P1 — Generar id único y persistir la nueva editorial
    private void guardarNuevaEditorial(final EditorialDominio datos) {
        UUID id = UtilUUID.generar();
        while (!UtilObjeto.esNulo(daoFactory.getEditorialDAO().consultarPorId(id))) {
            id = UtilUUID.generar();
        }
        final EditorialEntidad nueva = new EditorialEntidad.Builder()
                .id(id)
                .nit(datos.getNit())
                .nombre(datos.getNombre())
                .build();
        daoFactory.getEditorialDAO().crear(nueva);
    }
}
