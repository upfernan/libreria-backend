package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.LibroDTO;
import com.libreria.negocio.fachada.libro.impl.RegistrarLibroFachadaImpl;
import com.libreria.negocio.fachada.libro.impl.ActualizarLibroFachadaImpl;
import com.libreria.negocio.fachada.libro.impl.RetirarLibroFachadaImpl;
import com.libreria.negocio.fachada.libro.impl.ConsultarLibroPorIdFachadaImpl;
import com.libreria.negocio.fachada.libro.impl.ConsultarTodosLibrosFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody LibroDTO datos) {
        new RegistrarLibroFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El libro se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody LibroDTO datos) {
        var datosConId = new LibroDTO.Builder()
                .id(id)
                .titulo(datos.getTitulo())
                .tipoLibro(datos.getTipoLibro())
                .categoria(datos.getCategoria())
                .editorial(datos.getEditorial())
                .disponibles(datos.getDisponibles())
                .build();
        new ActualizarLibroFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("El libro se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarLibroFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El libro se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<LibroDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarLibroPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Libro consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<LibroDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosLibrosFachadaImpl().ejecutar(new LibroDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Libros consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
