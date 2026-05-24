package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.EstadoReservaDTO;
import com.libreria.negocio.fachada.estadoreserva.AgregarEstadoReservaFachada;
import com.libreria.negocio.fachada.estadoreserva.ActualizarEstadoReservaFachada;
import com.libreria.negocio.fachada.estadoreserva.RetirarEstadoReservaFachada;
import com.libreria.negocio.fachada.estadoreserva.ConsultarTodosEstadosReservaFachada;
import com.libreria.negocio.fachada.estadoreserva.impl.AgregarEstadoReservaFachadaImpl;
import com.libreria.negocio.fachada.estadoreserva.impl.ActualizarEstadoReservaFachadaImpl;
import com.libreria.negocio.fachada.estadoreserva.impl.RetirarEstadoReservaFachadaImpl;
import com.libreria.negocio.fachada.estadoreserva.impl.ConsultarTodosEstadosReservaFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/estados-reserva")
public class EstadoReservaControlador {

    private static final Logger logger = LoggerFactory.getLogger(EstadoReservaControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody EstadoReservaDTO datos) {
        logger.info("Iniciando agregar estadoReserva.");
        AgregarEstadoReservaFachada fachada = new AgregarEstadoReservaFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("El estado de reserva se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El estado de reserva se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EstadoReservaDTO datos) {
        logger.info("Iniciando actualizacion de estadoReserva con id: {}", id);
        var datosConId = new EstadoReservaDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        ActualizarEstadoReservaFachada fachada = new ActualizarEstadoReservaFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("El estado de reserva se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El estado de reserva se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de estadoReserva con id: {}", id);
        RetirarEstadoReservaFachada fachada = new RetirarEstadoReservaFachadaImpl();
        fachada.ejecutar(id);
        logger.info("El estado de reserva se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El estado de reserva se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EstadoReservaDTO>>> consultarTodos() {
        logger.info("Consultando todos los estadosReserva.");
        ConsultarTodosEstadosReservaFachada fachada = new ConsultarTodosEstadosReservaFachadaImpl();
        var resultado = fachada.ejecutar(new EstadoReservaDTO.Builder().build());
        logger.info("Estados de reserva consultados exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Estados de reserva consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
