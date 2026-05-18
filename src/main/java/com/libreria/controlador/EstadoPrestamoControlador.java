package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.EstadoPrestamoDTO;
import com.libreria.negocio.fachada.estadoprestamo.impl.AgregarEstadoPrestamoFachadaImpl;
import com.libreria.negocio.fachada.estadoprestamo.impl.ActualizarEstadoPrestamoFachadaImpl;
import com.libreria.negocio.fachada.estadoprestamo.impl.RetirarEstadoPrestamoFachadaImpl;
import com.libreria.negocio.fachada.estadoprestamo.impl.ConsultarTodosEstadosPrestamoFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/estados-prestamo")
public class EstadoPrestamoControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody EstadoPrestamoDTO datos) {
        new AgregarEstadoPrestamoFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El estado de préstamo se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EstadoPrestamoDTO datos) {
        var datosConId = new EstadoPrestamoDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        new ActualizarEstadoPrestamoFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("El estado de préstamo se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarEstadoPrestamoFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El estado de préstamo se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EstadoPrestamoDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosEstadosPrestamoFachadaImpl().ejecutar(new EstadoPrestamoDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Estados de préstamo consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
