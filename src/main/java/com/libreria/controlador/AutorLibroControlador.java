package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.AutorLibroDTO;
import com.libreria.negocio.fachada.autorlibro.impl.AsociarAutorLibroFachadaImpl;
import com.libreria.negocio.fachada.autorlibro.impl.QuitarAutorLibroFachadaImpl;
import com.libreria.negocio.fachada.autorlibro.impl.ConsultarAutorLibroPorIdFachadaImpl;
import com.libreria.negocio.fachada.autorlibro.impl.ConsultarAutoresPorLibroFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/autores-libro")
public class AutorLibroControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> asociar(@RequestBody AutorLibroDTO datos) {
        new AsociarAutorLibroFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El autor se asoció al libro exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> quitar(@PathVariable UUID id) {
        new QuitarAutorLibroFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El autor fue quitado del libro exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<AutorLibroDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarAutorLibroPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Autor-libro consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<RespuestaExito<List<AutorLibroDTO>>> consultarPorLibro(@PathVariable UUID libroId) {
        var resultado = new ConsultarAutoresPorLibroFachadaImpl().ejecutar(libroId);
        return new ResponseEntity<>(RespuestaExito.crear("Autores del libro consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
