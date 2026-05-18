package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.PagoDTO;
import com.libreria.negocio.fachada.pago.impl.RecibirPagoMultaFachadaImpl;
import com.libreria.negocio.fachada.pago.impl.ConsultarPagoPorIdFachadaImpl;
import com.libreria.negocio.fachada.pago.impl.ConsultarTodosPagosFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> recibir(@RequestBody PagoDTO datos) {
        new RecibirPagoMultaFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El pago se recibió exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<PagoDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarPagoPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Pago consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<PagoDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosPagosFachadaImpl().ejecutar(new PagoDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Pagos consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
