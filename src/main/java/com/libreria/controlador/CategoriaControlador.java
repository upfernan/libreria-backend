package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.CategoriaDTO;
import com.libreria.negocio.fachada.categoria.AgregarCategoriaFachada;
import com.libreria.negocio.fachada.categoria.ActualizarCategoriaFachada;
import com.libreria.negocio.fachada.categoria.RetirarCategoriaFachada;
import com.libreria.negocio.fachada.categoria.ConsultarCategoriaPorIdFachada;
import com.libreria.negocio.fachada.categoria.ConsultarTodasCategoriasFachada;
import com.libreria.negocio.fachada.categoria.impl.AgregarCategoriaFachadaImpl;
import com.libreria.negocio.fachada.categoria.impl.ActualizarCategoriaFachadaImpl;
import com.libreria.negocio.fachada.categoria.impl.RetirarCategoriaFachadaImpl;
import com.libreria.negocio.fachada.categoria.impl.ConsultarCategoriaPorIdFachadaImpl;
import com.libreria.negocio.fachada.categoria.impl.ConsultarTodasCategoriasFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaControlador {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody CategoriaDTO datos) {
        logger.info("Entre al metodo agregar del controlador...");
        AgregarCategoriaFachada fachada = new AgregarCategoriaFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("Sali del metodo agregar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La categoría se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody CategoriaDTO datos) {
        logger.info("Entre al metodo actualizar del controlador...");
        var datosConId = new CategoriaDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        ActualizarCategoriaFachada fachada = new ActualizarCategoriaFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("Sali del metodo actualizar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La categoría se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Entre al metodo retirar del controlador...");
        RetirarCategoriaFachada fachada = new RetirarCategoriaFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo retirar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La categoría se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<CategoriaDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Entre al metodo consultarPorId del controlador...");
        ConsultarCategoriaPorIdFachada fachada = new ConsultarCategoriaPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Sali del metodo consultarPorId del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Categoría consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<CategoriaDTO>>> consultarTodas() {
        logger.info("Entre al metodo consultarTodas del controlador...");
        ConsultarTodasCategoriasFachada fachada = new ConsultarTodasCategoriasFachadaImpl();
        var resultado = fachada.ejecutar(new CategoriaDTO.Builder().build());
        logger.info("Sali del metodo consultarTodas del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Categorías consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
