package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.DevolucionDTO;
import com.libreria.negocio.fachada.devolucion.impl.RecibirDevolucionFachadaImpl;
import com.libreria.negocio.fachada.devolucion.impl.ConsultarDevolucionPorIdFachadaImpl;
import com.libreria.negocio.fachada.devolucion.impl.ConsultarTodasDevolucionesFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devoluciones")
public class DevolucionControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> recibir(@RequestBody DevolucionDTO datos) {
        new RecibirDevolucionFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("La devolución se recibió exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<DevolucionDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarDevolucionPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Devolución consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<DevolucionDTO>>> consultarTodas() {
        var resultado = new ConsultarTodasDevolucionesFachadaImpl().ejecutar(new DevolucionDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Devoluciones consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
