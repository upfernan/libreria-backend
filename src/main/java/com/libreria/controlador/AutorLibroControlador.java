package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.AutorLibroDTO;
import com.libreria.negocio.fachada.autorlibro.AsociarAutorLibroFachada;
import com.libreria.negocio.fachada.autorlibro.QuitarAutorLibroFachada;
import com.libreria.negocio.fachada.autorlibro.ConsultarAutorLibroPorIdFachada;
import com.libreria.negocio.fachada.autorlibro.ConsultarTodosAutoresLibroFachada;
import com.libreria.negocio.fachada.autorlibro.impl.AsociarAutorLibroFachadaImpl;
import com.libreria.negocio.fachada.autorlibro.impl.QuitarAutorLibroFachadaImpl;
import com.libreria.negocio.fachada.autorlibro.impl.ConsultarAutorLibroPorIdFachadaImpl;
import com.libreria.negocio.fachada.autorlibro.impl.ConsultarTodosAutoresLibroFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/autores-libro")
public class AutorLibroControlador {

    private static final Logger logger = LoggerFactory.getLogger(AutorLibroControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> asociar(@RequestBody AutorLibroDTO datos) {
        logger.info("Entre al metodo asociar del controlador...");
        AsociarAutorLibroFachada fachada = new AsociarAutorLibroFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("Sali del metodo asociar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El autor se asoció al libro exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> quitar(@PathVariable UUID id) {
        logger.info("Entre al metodo quitar del controlador...");
        QuitarAutorLibroFachada fachada = new QuitarAutorLibroFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo quitar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El autor fue quitado del libro exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<AutorLibroDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Entre al metodo consultarPorId del controlador...");
        ConsultarAutorLibroPorIdFachada fachada = new ConsultarAutorLibroPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Sali del metodo consultarPorId del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Autor-libro consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<AutorLibroDTO>>> consultarTodos() {
        logger.info("Entre al metodo consultarTodos del controlador...");
        ConsultarTodosAutoresLibroFachada fachada = new ConsultarTodosAutoresLibroFachadaImpl();
        var resultado = fachada.ejecutar(new AutorLibroDTO.Builder().build());
        logger.info("Sali del metodo consultarTodos del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Autores-libro consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
