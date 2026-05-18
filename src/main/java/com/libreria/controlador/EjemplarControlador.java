package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.EjemplarDTO;
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

@RestController
@RequestMapping("/api/v1/ejemplares")
public class EjemplarControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody EjemplarDTO datos) {
        new RegistrarEjemplarFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EjemplarDTO datos) {
        var datosConId = new EjemplarDTO.Builder()
                .id(id)
                .libro(datos.getLibro())
                .signatura(datos.getSignatura())
                .build();
        new ActualizarEjemplarFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarEjemplarFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El ejemplar se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<EjemplarDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarEjemplarPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Ejemplar consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EjemplarDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosEjemplaresFachadaImpl().ejecutar(new EjemplarDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Ejemplares consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
