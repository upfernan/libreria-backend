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
        logger.info("Entre al metodo registrar del controlador...");
        RegistrarEjemplarFachada fachada = new RegistrarEjemplarFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("Sali del metodo registrar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EjemplarDTO datos) {
        logger.info("Entre al metodo actualizar del controlador...");
        var datosConId = new EjemplarDTO.Builder()
                .id(id)
                .libro(datos.getLibro())
                .signatura(datos.getSignatura())
                .build();
        ActualizarEjemplarFachada fachada = new ActualizarEjemplarFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("Sali del metodo actualizar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Entre al metodo retirar del controlador...");
        RetirarEjemplarFachada fachada = new RetirarEjemplarFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo retirar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<EjemplarDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Entre al metodo consultarPorId del controlador...");
        ConsultarEjemplarPorIdFachada fachada = new ConsultarEjemplarPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Sali del metodo consultarPorId del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Ejemplar consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EjemplarDTO>>> consultarTodos() {
        logger.info("Entre al metodo consultarTodos del controlador...");
        ConsultarTodosEjemplaresFachada fachada = new ConsultarTodosEjemplaresFachadaImpl();
        var resultado = fachada.ejecutar(new EjemplarDTO.Builder().build());
        logger.info("Sali del metodo consultarTodos del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Ejemplares consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
