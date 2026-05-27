package com.libreria.negocio.casouso.autor.impl;

import java.util.UUID;

import com.libreria.datos.dao.sql.factoria.DAOFactory;
import com.libreria.entidad.AutorEntidad;
import com.libreria.negocio.casouso.autor.AgregarAutorCasoUso;
import com.libreria.negocio.dominio.AutorDominio;
import com.libreria.transversal.utilitario.UtilObjeto;
import com.libreria.transversal.utilitario.UtilTexto;
import com.libreria.transversal.utilitario.UtilUUID;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;

public class AgregarAutorCasoUsoImpl implements AgregarAutorCasoUso {

    private static final String SUFIJO_CARACTERES = " caracteres.";

    private final DAOFactory daoFactory;

    public AgregarAutorCasoUsoImpl(final DAOFactory daoFactory) {
        super();
        this.daoFactory = daoFactory;
    }

    @Override
    public void ejecutar(final AutorDominio datos) {
        // P2 — Validar tipo de dato, obligatoriedad, longitud y formato de los datos de entrada
        validarDatosObligatorios(datos);
        // P1 — Registrar el autor garantizando identificador único
        guardarNuevoAutor(datos);
    }

    // P2 — Datos requeridos válidos en tipo, obligatoriedad y longitud
    private void validarDatosObligatorios(final AutorDominio datos) {
        if (UtilObjeto.esNulo(datos)) {
            throw GestorLibreriaExcepcion.crear("Los datos del autor son obligatorios.", "Se recibió un objeto AutorDominio nulo.");
        }
        if (UtilTexto.esNula(datos.getPrimerNombre())) {
            throw GestorLibreriaExcepcion.crear("El primer nombre del autor es obligatorio.", "El campo primerNombre llegó nulo en AutorDominio.");
        }
        if (!UtilTexto.tieneLongitudValida(datos.getPrimerNombre(), 1, 20)) {
            throw GestorLibreriaExcepcion.crear("El primer nombre del autor debe tener entre 1 y 20 caracteres.", "primerNombre con longitud inválida en AutorDominio: " + datos.getPrimerNombre().length() + SUFIJO_CARACTERES);
        }
        if (UtilObjeto.esNulo(datos.getSegundoNombre())) {
            throw GestorLibreriaExcepcion.crear("El segundo nombre del autor es obligatorio.", "El campo segundoNombre llegó nulo en AutorDominio.");
        }
        if (!datos.getSegundoNombre().isEmpty() && !UtilTexto.tieneLongitudValida(datos.getSegundoNombre(), 1, 20)) {
            throw GestorLibreriaExcepcion.crear("El segundo nombre del autor debe tener entre 1 y 20 caracteres si se indica.", "segundoNombre con longitud inválida en AutorDominio: " + datos.getSegundoNombre().length() + SUFIJO_CARACTERES);
        }
        if (UtilTexto.esNula(datos.getPrimerApellido())) {
            throw GestorLibreriaExcepcion.crear("El primer apellido del autor es obligatorio.", "El campo primerApellido llegó nulo en AutorDominio.");
        }
        if (!UtilTexto.tieneLongitudValida(datos.getPrimerApellido(), 1, 20)) {
            throw GestorLibreriaExcepcion.crear("El primer apellido del autor debe tener entre 1 y 20 caracteres.", "primerApellido con longitud inválida en AutorDominio: " + datos.getPrimerApellido().length() + SUFIJO_CARACTERES);
        }
        if (UtilObjeto.esNulo(datos.getSegundoApellido())) {
            throw GestorLibreriaExcepcion.crear("El segundo apellido del autor es obligatorio.", "El campo segundoApellido llegó nulo en AutorDominio.");
        }
        if (!datos.getSegundoApellido().isEmpty() && !UtilTexto.tieneLongitudValida(datos.getSegundoApellido(), 1, 20)) {
            throw GestorLibreriaExcepcion.crear("El segundo apellido del autor debe tener entre 1 y 20 caracteres si se indica.", "segundoApellido con longitud inválida en AutorDominio: " + datos.getSegundoApellido().length() + SUFIJO_CARACTERES);
        }
    }

    // P1 — Generar id único y persistir el nuevo autor
    private void guardarNuevoAutor(final AutorDominio datos) {
        UUID id = UtilUUID.generar();
        while (UtilUUID.tieneValor(daoFactory.getAutorDAO().consultarPorId(id).getId())) {
            id = UtilUUID.generar();
        }
        final AutorEntidad nuevo = new AutorEntidad.Builder()
                .id(id)
                .primerNombre(datos.getPrimerNombre())
                .segundoNombre(datos.getSegundoNombre())
                .primerApellido(datos.getPrimerApellido())
                .segundoApellido(datos.getSegundoApellido())
                .build();
        daoFactory.getAutorDAO().crear(nuevo);
    }
}
