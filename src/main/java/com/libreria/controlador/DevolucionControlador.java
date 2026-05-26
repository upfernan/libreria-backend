package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.DevolucionDTO;
import com.libreria.negocio.fachada.devolucion.RecibirDevolucionFachada;
import com.libreria.negocio.fachada.devolucion.ConsultarDevolucionPorIdFachada;
import com.libreria.negocio.fachada.devolucion.ConsultarTodasDevolucionesFachada;
import com.libreria.negocio.fachada.devolucion.impl.RecibirDevolucionFachadaImpl;
import com.libreria.negocio.fachada.devolucion.impl.ConsultarDevolucionPorIdFachadaImpl;
import com.libreria.negocio.fachada.devolucion.impl.ConsultarTodasDevolucionesFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/devoluciones")
public class DevolucionControlador {

    private static final Logger logger = LoggerFactory.getLogger(DevolucionControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> recibir(@RequestBody DevolucionDTO datos) {
        logger.info("Entre al metodo recibir del controlador...");
        RecibirDevolucionFachada fachada = new RecibirDevolucionFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("Sali del metodo recibir del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La devolución se recibió exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<DevolucionDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Entre al metodo consultarPorId del controlador...");
        ConsultarDevolucionPorIdFachada fachada = new ConsultarDevolucionPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Sali del metodo consultarPorId del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Devolución consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<DevolucionDTO>>> consultarTodas() {
        logger.info("Entre al metodo consultarTodas del controlador...");
        ConsultarTodasDevolucionesFachada fachada = new ConsultarTodasDevolucionesFachadaImpl();
        var resultado = fachada.ejecutar(new DevolucionDTO.Builder().build());
        logger.info("Sali del metodo consultarTodas del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Devoluciones consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
