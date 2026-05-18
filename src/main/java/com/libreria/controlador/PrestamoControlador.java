package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.PrestamoDTO;
import com.libreria.negocio.fachada.prestamo.impl.RegistrarPrestamoFachadaImpl;
import com.libreria.negocio.fachada.prestamo.impl.CerrarPrestamoFachadaImpl;
import com.libreria.negocio.fachada.prestamo.impl.ConsultarPrestamoPorIdFachadaImpl;
import com.libreria.negocio.fachada.prestamo.impl.ConsultarTodosPrestamosFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody PrestamoDTO datos) {
        new RegistrarPrestamoFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El préstamo se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<RespuestaExito<String>> cerrar(@PathVariable UUID id) {
        new CerrarPrestamoFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El préstamo se cerró exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<PrestamoDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarPrestamoPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Préstamo consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<PrestamoDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosPrestamosFachadaImpl().ejecutar(new PrestamoDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Préstamos consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
