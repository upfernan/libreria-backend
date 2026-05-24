package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.EjemplarDTO;
import com.libreria.negocio.fachada.ejemplar.RegistrarEjemplarFachada;
import com.libreria.negocio.fachada.ejemplar.ActualizarEjemplarFachada;
import com.libreria.negocio.fachada.ejemplar.RetirarEjemplarFachada;
import com.libreria.negocio.fachada.ejemplar.ConsultarEjemplarPorIdFachada;
import com.libreria.negocio.fachada.ejemplar.ConsultarTodosEjemplaresFachada;
import com.libreria.negocio.fachada.ejemplar.impl.RegistrarEjemplarFachadaImpl;
import com.libreria.negocio.fachada.ejemplar.impl.ActualizarEjemplarFachadaImpl;
import com.libreria.negocio.fachada.ejemplar.impl.RetirarEjemplarFachadaImpl;
import com.libreria.negocio.fachada.ejemplar.impl.ConsultarEjemplarPorIdFachadaImpl;
import com.libreria.negocio.fachada.ejemplar.impl.ConsultarTodosEjemplaresFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/ejemplares")
public class EjemplarControlador {

    private static final Logger logger = LoggerFactory.getLogger(EjemplarControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody EjemplarDTO datos) {
        logger.info("Iniciando registro de ejemplar.");
        RegistrarEjemplarFachada fachada = new RegistrarEjemplarFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("El ejemplar se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EjemplarDTO datos) {
        logger.info("Iniciando actualizacion de ejemplar con id: {}", id);
        var datosConId = new EjemplarDTO.Builder()
                .id(id)
                .libro(datos.getLibro())
                .signatura(datos.getSignatura())
                .build();
        ActualizarEjemplarFachada fachada = new ActualizarEjemplarFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("El ejemplar se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de ejemplar con id: {}", id);
        RetirarEjemplarFachada fachada = new RetirarEjemplarFachadaImpl();
        fachada.ejecutar(id);
        logger.info("El ejemplar se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<EjemplarDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando ejemplar por id: {}", id);
        ConsultarEjemplarPorIdFachada fachada = new ConsultarEjemplarPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Ejemplar consultado exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Ejemplar consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EjemplarDTO>>> consultarTodos() {
        logger.info("Consultando todos los ejemplares.");
        ConsultarTodosEjemplaresFachada fachada = new ConsultarTodosEjemplaresFachadaImpl();
        var resultado = fachada.ejecutar(new EjemplarDTO.Builder().build());
        logger.info("Ejemplares consultados exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Ejemplares consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
