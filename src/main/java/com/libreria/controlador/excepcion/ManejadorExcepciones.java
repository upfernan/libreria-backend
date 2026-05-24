package com.libreria.controlador.excepcion;

import com.libreria.controlador.respuesta.RespuestaError;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorExcepciones {

    private static final Logger logger = LoggerFactory.getLogger(ManejadorExcepciones.class);

    @ExceptionHandler(GestorLibreriaExcepcion.class)
    public ResponseEntity<RespuestaError> gestionar(GestorLibreriaExcepcion ex) {
        logger.error("Error controlado: {}", ex.getMensajeTecnico(), ex.getExcepcionRaiz());
        return new ResponseEntity<>(RespuestaError.crear(ex.getMensajeUsuario()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> gestionar(Exception ex) {
        var mensajeTecnico = "Se ha generado una excepción no controlada en el sistema. Por favor revisar los logs de errores.";
        var mensajeUsuario = "Ha ocurrido un error inesperado. Por favor inténtelo de nuevo y si el problema persiste contacte a un administrador.";
        logger.error(mensajeTecnico, ex);
        return new ResponseEntity<>(RespuestaError.crear(mensajeUsuario), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
