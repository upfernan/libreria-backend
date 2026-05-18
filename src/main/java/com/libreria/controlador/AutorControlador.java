package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.AutorDTO;
import com.libreria.negocio.fachada.autor.impl.AgregarAutorFachadaImpl;
import com.libreria.negocio.fachada.autor.impl.ActualizarAutorFachadaImpl;
import com.libreria.negocio.fachada.autor.impl.RetirarAutorFachadaImpl;
import com.libreria.negocio.fachada.autor.impl.ConsultarAutorPorIdFachadaImpl;
import com.libreria.negocio.fachada.autor.impl.ConsultarTodosAutoresFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody AutorDTO datos) {
        new AgregarAutorFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El autor se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody AutorDTO datos) {
        var datosConId = new AutorDTO.Builder()
                .id(id)
                .primerNombre(datos.getPrimerNombre())
                .segundoNombre(datos.getSegundoNombre())
                .primerApellido(datos.getPrimerApellido())
                .segundoApellido(datos.getSegundoApellido())
                .build();
        new ActualizarAutorFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("El autor se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarAutorFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El autor se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<AutorDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarAutorPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Autor consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<AutorDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosAutoresFachadaImpl().ejecutar(new AutorDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Autores consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
