package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.TipoLibroDTO;
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

@RestController
@RequestMapping("/api/v1/tipos-libro")
public class TipoLibroControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody TipoLibroDTO datos) {
        new AgregarTipoLibroFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de libro se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody TipoLibroDTO datos) {
        var datosConId = new TipoLibroDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        new ActualizarTipoLibroFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de libro se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarTipoLibroFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de libro se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<TipoLibroDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarTipoLibroPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Tipo de libro consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<TipoLibroDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosTiposLibroFachadaImpl().ejecutar(new TipoLibroDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Tipos de libro consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
