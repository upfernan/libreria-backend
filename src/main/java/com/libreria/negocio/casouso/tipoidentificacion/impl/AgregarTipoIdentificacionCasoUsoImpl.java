package com.libreria.negocio.casouso.tipoidentificacion.impl;

import java.util.List;
import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.TipoIdentificacionEntidad;
import com.libreria.negocio.casouso.tipoidentificacion.AgregarTipoIdentificacionCasoUso;
import com.libreria.negocio.dominio.TipoIdentificacionDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarTipoIdentificacionCasoUsoImpl implements AgregarTipoIdentificacionCasoUso {

    private final DAOFactory daoFactory;

    public AgregarTipoIdentificacionCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final TipoIdentificacionDominio datos) {
        // P3 — Validar tipo de dato, obligatoriedad y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P2 — Validar que no exista otro tipo de identificación con el mismo nombre
        validarNombreUnico(datos.getNombre());
        // P1 — Registrar el tipo de identificación garantizando identificador único
        guardarNuevoTipoIdentificacion(datos);
    }

    // P3 — Datos requeridos válidos en tipo, obligatoriedad y formato
    private void validarDatosObligatorios(final TipoIdentificacionDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del tipo de identificación son obligatorios.", "Se recibió un objeto TipoIdentificacionDominio nulo.");
        }
        if (UtilTexto.esNula(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre del tipo de identificación es obligatorio.", "El campo nombre llegó nulo o vacío en TipoIdentificacionDominio.");
        }
        if (!UtilTexto.soloLetrasMayusculasYEspacios(datos.getNombre())) {
            throw GestorLibreriaExcepcion.crear("El nombre del tipo de identificación solo puede contener letras mayúsculas y espacios.", "nombre con formato inválido en TipoIdentificacionDominio: " + datos.getNombre());
        }
        if (UtilTexto.esNula(datos.getDescripcion())) {
            throw GestorLibreriaExcepcion.crear("La descripción del tipo de identificación es obligatoria.", "El campo descripcion llegó nulo o vacío en TipoIdentificacionDominio.");
        }
    }

    // P2 — Validar que no exista otro tipo de identificación con el mismo nombre
    private void validarNombreUnico(final String nombre) {
        final TipoIdentificacionEntidad filtro = new TipoIdentificacionEntidad.Builder().nombre(nombre).build();
        final List<TipoIdentificacionEntidad> existentes = daoFactory.getTipoIdentificacionDAO().consultarPorFiltro(filtro);
        if (!UtilObjeto.esNulo(existentes) && !existentes.isEmpty()) {
            throw GestorLibreriaExcepcion.crear("Ya existe un tipo de identificación con ese nombre.", "nombre duplicado en tipoIdentificacion: " + nombre);
        }
    }

    // P1 — Generar id único y persistir el nuevo tipo de identificación
    private void guardarNuevoTipoIdentificacion(final TipoIdentificacionDominio datos) {
        UUID id = UtilUUID.generar();
        while (UtilUUID.tieneValor(daoFactory.getTipoIdentificacionDAO().consultarPorId(id).getId())) {
            id = UtilUUID.generar();
        }
        final TipoIdentificacionEntidad nueva = new TipoIdentificacionEntidad.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        daoFactory.getTipoIdentificacionDAO().crear(nueva);
    }
}
