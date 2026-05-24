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
        logger.info("Iniciando recepcion de devolucion.");
        RecibirDevolucionFachada fachada = new RecibirDevolucionFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("La devolución se recibió exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La devolución se recibió exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<DevolucionDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando devolucion por id: {}", id);
        ConsultarDevolucionPorIdFachada fachada = new ConsultarDevolucionPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Devolución consultada exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Devolución consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<DevolucionDTO>>> consultarTodas() {
        logger.info("Consultando todas las devoluciones.");
        ConsultarTodasDevolucionesFachada fachada = new ConsultarTodasDevolucionesFachadaImpl();
        var resultado = fachada.ejecutar(new DevolucionDTO.Builder().build());
        logger.info("Devoluciones consultadas exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Devoluciones consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
