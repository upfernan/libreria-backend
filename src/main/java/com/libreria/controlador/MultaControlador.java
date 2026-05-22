package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.MultaDTO;
import com.libreria.negocio.fachada.multa.impl.CobrarMultaFachadaImpl;
import com.libreria.negocio.fachada.multa.impl.ConsultarMultaPorIdFachadaImpl;
import com.libreria.negocio.fachada.multa.impl.ConsultarTodasMultasFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/multas")
public class MultaControlador {

    @PostMapping("/cobrar")
    public ResponseEntity<RespuestaExito<String>> cobrar(@RequestBody MultaDTO datos) {
        new CobrarMultaFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("La multa se cobró exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<MultaDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarMultaPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Multa consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<MultaDTO>>> consultarTodas() {
        var resultado = new ConsultarTodasMultasFachadaImpl().ejecutar(new MultaDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Multas consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
