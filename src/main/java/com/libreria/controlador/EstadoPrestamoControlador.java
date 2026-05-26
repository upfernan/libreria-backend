package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.EstadoPrestamoDTO;
import com.libreria.negocio.fachada.estadoprestamo.AgregarEstadoPrestamoFachada;
import com.libreria.negocio.fachada.estadoprestamo.ActualizarEstadoPrestamoFachada;
import com.libreria.negocio.fachada.estadoprestamo.RetirarEstadoPrestamoFachada;
import com.libreria.negocio.fachada.estadoprestamo.ConsultarTodosEstadosPrestamoFachada;
import com.libreria.negocio.fachada.estadoprestamo.impl.AgregarEstadoPrestamoFachadaImpl;
import com.libreria.negocio.fachada.estadoprestamo.impl.ActualizarEstadoPrestamoFachadaImpl;
import com.libreria.negocio.fachada.estadoprestamo.impl.RetirarEstadoPrestamoFachadaImpl;
import com.libreria.negocio.fachada.estadoprestamo.impl.ConsultarTodosEstadosPrestamoFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/estados-prestamo")
public class EstadoPrestamoControlador {

    private static final Logger logger = LoggerFactory.getLogger(EstadoPrestamoControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody EstadoPrestamoDTO datos) {
        logger.info("Entre al metodo agregar del controlador...");
        AgregarEstadoPrestamoFachada fachada = new AgregarEstadoPrestamoFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("Sali del metodo agregar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El estado de préstamo se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EstadoPrestamoDTO datos) {
        logger.info("Entre al metodo actualizar del controlador...");
        var datosConId = new EstadoPrestamoDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        ActualizarEstadoPrestamoFachada fachada = new ActualizarEstadoPrestamoFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("Sali del metodo actualizar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El estado de préstamo se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Entre al metodo retirar del controlador...");
        RetirarEstadoPrestamoFachada fachada = new RetirarEstadoPrestamoFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo retirar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El estado de préstamo se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EstadoPrestamoDTO>>> consultarTodos() {
        logger.info("Entre al metodo consultarTodos del controlador...");
        ConsultarTodosEstadosPrestamoFachada fachada = new ConsultarTodosEstadosPrestamoFachadaImpl();
        var resultado = fachada.ejecutar(new EstadoPrestamoDTO.Builder().build());
        logger.info("Sali del metodo consultarTodos del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Estados de préstamo consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
