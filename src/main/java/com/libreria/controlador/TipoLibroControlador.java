package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.TipoLibroDTO;
import com.libreria.negocio.fachada.tipolibro.AgregarTipoLibroFachada;
import com.libreria.negocio.fachada.tipolibro.ActualizarTipoLibroFachada;
import com.libreria.negocio.fachada.tipolibro.RetirarTipoLibroFachada;
import com.libreria.negocio.fachada.tipolibro.ConsultarTipoLibroPorIdFachada;
import com.libreria.negocio.fachada.tipolibro.ConsultarTodosTiposLibroFachada;
import com.libreria.negocio.fachada.tipolibro.impl.AgregarTipoLibroFachadaImpl;
import com.libreria.negocio.fachada.tipolibro.impl.ActualizarTipoLibroFachadaImpl;
import com.libreria.negocio.fachada.tipolibro.impl.RetirarTipoLibroFachadaImpl;
import com.libreria.negocio.fachada.tipolibro.impl.ConsultarTipoLibroPorIdFachadaImpl;
import com.libreria.negocio.fachada.tipolibro.impl.ConsultarTodosTiposLibroFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/tipos-libro")
public class TipoLibroControlador {

    private static final Logger logger = LoggerFactory.getLogger(TipoLibroControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody TipoLibroDTO datos) {
        logger.info("Entre al metodo agregar del controlador...");
        AgregarTipoLibroFachada fachada = new AgregarTipoLibroFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("Sali del metodo agregar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de libro se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody TipoLibroDTO datos) {
        logger.info("Entre al metodo actualizar del controlador...");
        var datosConId = new TipoLibroDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        ActualizarTipoLibroFachada fachada = new ActualizarTipoLibroFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("Sali del metodo actualizar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de libro se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Entre al metodo retirar del controlador...");
        RetirarTipoLibroFachada fachada = new RetirarTipoLibroFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo retirar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de libro se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<TipoLibroDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Entre al metodo consultarPorId del controlador...");
        ConsultarTipoLibroPorIdFachada fachada = new ConsultarTipoLibroPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Sali del metodo consultarPorId del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Tipo de libro consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<TipoLibroDTO>>> consultarTodos() {
        logger.info("Entre al metodo consultarTodos del controlador...");
        ConsultarTodosTiposLibroFachada fachada = new ConsultarTodosTiposLibroFachadaImpl();
        var resultado = fachada.ejecutar(new TipoLibroDTO.Builder().build());
        logger.info("Sali del metodo consultarTodos del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Tipos de libro consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
