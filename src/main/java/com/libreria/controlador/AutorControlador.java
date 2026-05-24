package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.AutorDTO;
import com.libreria.negocio.fachada.autor.AgregarAutorFachada;
import com.libreria.negocio.fachada.autor.ActualizarAutorFachada;
import com.libreria.negocio.fachada.autor.RetirarAutorFachada;
import com.libreria.negocio.fachada.autor.ConsultarAutorPorIdFachada;
import com.libreria.negocio.fachada.autor.ConsultarTodosAutoresFachada;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorControlador {

    private static final Logger logger = LoggerFactory.getLogger(AutorControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody AutorDTO datos) {
        logger.info("Iniciando agregar autor.");
        AgregarAutorFachada fachada = new AgregarAutorFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("El autor se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El autor se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody AutorDTO datos) {
        logger.info("Iniciando actualizacion de autor con id: {}", id);
        var datosConId = new AutorDTO.Builder()
                .id(id)
                .primerNombre(datos.getPrimerNombre())
                .segundoNombre(datos.getSegundoNombre())
                .primerApellido(datos.getPrimerApellido())
                .segundoApellido(datos.getSegundoApellido())
                .build();
        ActualizarAutorFachada fachada = new ActualizarAutorFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("El autor se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El autor se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de autor con id: {}", id);
        RetirarAutorFachada fachada = new RetirarAutorFachadaImpl();
        fachada.ejecutar(id);
        logger.info("El autor se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El autor se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<AutorDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando autor por id: {}", id);
        ConsultarAutorPorIdFachada fachada = new ConsultarAutorPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Autor consultado exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Autor consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<AutorDTO>>> consultarTodos() {
        logger.info("Consultando todos los autores.");
        ConsultarTodosAutoresFachada fachada = new ConsultarTodosAutoresFachadaImpl();
        var resultado = fachada.ejecutar(new AutorDTO.Builder().build());
        logger.info("Autores consultados exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Autores consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
