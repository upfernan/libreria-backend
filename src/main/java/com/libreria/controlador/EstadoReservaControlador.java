package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.EstadoReservaDTO;
import com.libreria.negocio.fachada.estadoreserva.impl.AgregarEstadoReservaFachadaImpl;
import com.libreria.negocio.fachada.estadoreserva.impl.ActualizarEstadoReservaFachadaImpl;
import com.libreria.negocio.fachada.estadoreserva.impl.RetirarEstadoReservaFachadaImpl;
import com.libreria.negocio.fachada.estadoreserva.impl.ConsultarTodosEstadosReservaFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/estados-reserva")
public class EstadoReservaControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody EstadoReservaDTO datos) {
        new AgregarEstadoReservaFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El estado de reserva se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EstadoReservaDTO datos) {
        var datosConId = new EstadoReservaDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        new ActualizarEstadoReservaFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("El estado de reserva se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarEstadoReservaFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El estado de reserva se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EstadoReservaDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosEstadosReservaFachadaImpl().ejecutar(new EstadoReservaDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Estados de reserva consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
