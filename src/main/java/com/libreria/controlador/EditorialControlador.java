package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.EditorialDTO;
import com.libreria.negocio.fachada.editorial.AgregarEditorialFachada;
import com.libreria.negocio.fachada.editorial.ActualizarEditorialFachada;
import com.libreria.negocio.fachada.editorial.RetirarEditorialFachada;
import com.libreria.negocio.fachada.editorial.ConsultarEditorialPorIdFachada;
import com.libreria.negocio.fachada.editorial.ConsultarTodasEditorialesFachada;
import com.libreria.negocio.fachada.editorial.impl.AgregarEditorialFachadaImpl;
import com.libreria.negocio.fachada.editorial.impl.ActualizarEditorialFachadaImpl;
import com.libreria.negocio.fachada.editorial.impl.RetirarEditorialFachadaImpl;
import com.libreria.negocio.fachada.editorial.impl.ConsultarEditorialPorIdFachadaImpl;
import com.libreria.negocio.fachada.editorial.impl.ConsultarTodasEditorialesFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/editoriales")
public class EditorialControlador {

    private static final Logger logger = LoggerFactory.getLogger(EditorialControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody EditorialDTO datos) {
        logger.info("Iniciando agregar editorial.");
        AgregarEditorialFachada fachada = new AgregarEditorialFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("La editorial se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La editorial se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EditorialDTO datos) {
        logger.info("Iniciando actualizacion de editorial con id: {}", id);
        var datosConId = new EditorialDTO.Builder()
                .id(id)
                .nit(datos.getNit())
                .nombre(datos.getNombre())
                .build();
        ActualizarEditorialFachada fachada = new ActualizarEditorialFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("La editorial se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La editorial se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de editorial con id: {}", id);
        RetirarEditorialFachada fachada = new RetirarEditorialFachadaImpl();
        fachada.ejecutar(id);
        logger.info("La editorial se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La editorial se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<EditorialDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando editorial por id: {}", id);
        ConsultarEditorialPorIdFachada fachada = new ConsultarEditorialPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Editorial consultada exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Editorial consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EditorialDTO>>> consultarTodas() {
        logger.info("Consultando todas las editoriales.");
        ConsultarTodasEditorialesFachada fachada = new ConsultarTodasEditorialesFachadaImpl();
        var resultado = fachada.ejecutar(new EditorialDTO.Builder().build());
        logger.info("Editoriales consultadas exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Editoriales consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
