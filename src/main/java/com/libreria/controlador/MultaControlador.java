package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.MultaDTO;
import com.libreria.negocio.fachada.multa.impl.CobrarMultaFachadaImpl;
import com.libreria.negocio.fachada.multa.impl.ConsultarMultaPorIdFachadaImpl;
import com.libreria.negocio.fachada.multa.impl.ConsultarMultasPorUsuarioFachadaImpl;
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

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<RespuestaExito<List<MultaDTO>>> consultarPorUsuario(@PathVariable UUID usuarioId) {
        var resultado = new ConsultarMultasPorUsuarioFachadaImpl().ejecutar(usuarioId);
        return new ResponseEntity<>(RespuestaExito.crear("Multas del usuario consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
