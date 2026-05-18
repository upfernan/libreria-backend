package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.EditorialDTO;
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

@RestController
@RequestMapping("/api/v1/editoriales")
public class EditorialControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody EditorialDTO datos) {
        new AgregarEditorialFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("La editorial se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody EditorialDTO datos) {
        var datosConId = new EditorialDTO.Builder()
                .id(id)
                .nit(datos.getNit())
                .nombre(datos.getNombre())
                .build();
        new ActualizarEditorialFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("La editorial se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarEditorialFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("La editorial se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<EditorialDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarEditorialPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Editorial consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<EditorialDTO>>> consultarTodas() {
        var resultado = new ConsultarTodasEditorialesFachadaImpl().ejecutar(new EditorialDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Editoriales consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
