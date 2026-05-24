package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.PagoDTO;
import com.libreria.negocio.fachada.pago.RecibirPagoMultaFachada;
import com.libreria.negocio.fachada.pago.ConsultarPagoPorIdFachada;
import com.libreria.negocio.fachada.pago.ConsultarTodosPagosFachada;
import com.libreria.negocio.fachada.pago.impl.RecibirPagoMultaFachadaImpl;
import com.libreria.negocio.fachada.pago.impl.ConsultarPagoPorIdFachadaImpl;
import com.libreria.negocio.fachada.pago.impl.ConsultarTodosPagosFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoControlador {

    private static final Logger logger = LoggerFactory.getLogger(PagoControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> recibir(@RequestBody PagoDTO datos) {
        logger.info("Iniciando recepcion de pago.");
        RecibirPagoMultaFachada fachada = new RecibirPagoMultaFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("El pago se recibió exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El pago se recibió exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<PagoDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando pago por id: {}", id);
        ConsultarPagoPorIdFachada fachada = new ConsultarPagoPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Pago consultado exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Pago consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<PagoDTO>>> consultarTodos() {
        logger.info("Consultando todos los pagos.");
        ConsultarTodosPagosFachada fachada = new ConsultarTodosPagosFachadaImpl();
        var resultado = fachada.ejecutar(new PagoDTO.Builder().build());
        logger.info("Pagos consultados exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Pagos consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
