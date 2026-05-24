package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.MultaDTO;
import com.libreria.negocio.fachada.multa.CobrarMultaFachada;
import com.libreria.negocio.fachada.multa.ConsultarMultaPorIdFachada;
import com.libreria.negocio.fachada.multa.ConsultarTodasMultasFachada;
import com.libreria.negocio.fachada.multa.impl.CobrarMultaFachadaImpl;
import com.libreria.negocio.fachada.multa.impl.ConsultarMultaPorIdFachadaImpl;
import com.libreria.negocio.fachada.multa.impl.ConsultarTodasMultasFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/multas")
public class MultaControlador {

    private static final Logger logger = LoggerFactory.getLogger(MultaControlador.class);

    @PostMapping("/cobrar")
    public ResponseEntity<RespuestaExito<String>> cobrar(@RequestBody MultaDTO datos) {
        logger.info("Iniciando cobro de multa.");
        CobrarMultaFachada fachada = new CobrarMultaFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("La multa se cobró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La multa se cobró exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<MultaDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando multa por id: {}", id);
        ConsultarMultaPorIdFachada fachada = new ConsultarMultaPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Multa consultada exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Multa consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<MultaDTO>>> consultarTodas() {
        logger.info("Consultando todas las multas.");
        ConsultarTodasMultasFachada fachada = new ConsultarTodasMultasFachadaImpl();
        var resultado = fachada.ejecutar(new MultaDTO.Builder().build());
        logger.info("Multas consultadas exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Multas consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
