package com.libreria.controlador.excepcion;

import com.libreria.controlador.respuesta.RespuestaError;
import com.libreria.transversal.utilitario.excepcion.GestorLibreriaExcepcion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(GestorLibreriaExcepcion.class)
    public ResponseEntity<RespuestaError> gestionar(GestorLibreriaExcepcion ex) {
        System.err.println(ex.getMensajeTecnico());
        return new ResponseEntity<>(RespuestaError.crear(ex.getMensajeUsuario()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> gestionar(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(RespuestaError.crear("Ha ocurrido un error inesperado..."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
