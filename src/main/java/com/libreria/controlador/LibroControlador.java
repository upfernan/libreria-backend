package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.LibroDTO;
import com.libreria.negocio.fachada.libro.RegistrarLibroFachada;
import com.libreria.negocio.fachada.libro.ActualizarLibroFachada;
import com.libreria.negocio.fachada.libro.RetirarLibroFachada;
import com.libreria.negocio.fachada.libro.ConsultarLibroPorIdFachada;
import com.libreria.negocio.fachada.libro.ConsultarTodosLibrosFachada;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroControlador {

    private static final Logger logger = LoggerFactory.getLogger(LibroControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody LibroDTO datos) {
        logger.info("Iniciando registro de libro.");
        RegistrarLibroFachada fachada = new RegistrarLibroFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("El libro se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El libro se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody LibroDTO datos) {
        logger.info("Iniciando actualizacion de libro con id: {}", id);
        var datosConId = new LibroDTO.Builder()
                .id(id)
                .titulo(datos.getTitulo())
                .tipoLibro(datos.getTipoLibro())
                .categoria(datos.getCategoria())
                .editorial(datos.getEditorial())
                .disponibles(datos.getDisponibles())
                .build();
        ActualizarLibroFachada fachada = new ActualizarLibroFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("El libro se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El libro se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de libro con id: {}", id);
        RetirarLibroFachada fachada = new RetirarLibroFachadaImpl();
        fachada.ejecutar(id);
        logger.info("El libro se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El libro se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<LibroDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando libro por id: {}", id);
        ConsultarLibroPorIdFachada fachada = new ConsultarLibroPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Libro consultado exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Libro consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<LibroDTO>>> consultarTodos() {
        logger.info("Consultando todos los libros.");
        ConsultarTodosLibrosFachada fachada = new ConsultarTodosLibrosFachadaImpl();
        var resultado = fachada.ejecutar(new LibroDTO.Builder().build());
        logger.info("Libros consultados exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Libros consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
