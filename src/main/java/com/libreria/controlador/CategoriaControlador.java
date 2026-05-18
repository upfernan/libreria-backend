package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.CategoriaDTO;
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

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody CategoriaDTO datos) {
        new AgregarCategoriaFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("La categoría se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody CategoriaDTO datos) {
        var datosConId = new CategoriaDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        new ActualizarCategoriaFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("La categoría se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarCategoriaFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("La categoría se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<CategoriaDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarCategoriaPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Categoría consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<CategoriaDTO>>> consultarTodas() {
        var resultado = new ConsultarTodasCategoriasFachadaImpl().ejecutar(new CategoriaDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Categorías consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
