package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.TipoIdentificacionDTO;
import com.libreria.negocio.fachada.tipoidentificacion.impl.AgregarTipoIdentificacionFachadaImpl;
import com.libreria.negocio.fachada.tipoidentificacion.impl.ActualizarTipoIdentificacionFachadaImpl;
import com.libreria.negocio.fachada.tipoidentificacion.impl.RetirarTipoIdentificacionFachadaImpl;
import com.libreria.negocio.fachada.tipoidentificacion.impl.ConsultarTipoIdentificacionPorIdFachadaImpl;
import com.libreria.negocio.fachada.tipoidentificacion.impl.ConsultarTodosTiposIdentificacionFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tipos-identificacion")
public class TipoIdentificacionControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody TipoIdentificacionDTO datos) {
        new AgregarTipoIdentificacionFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de identificación se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody TipoIdentificacionDTO datos) {
        var datosConId = new TipoIdentificacionDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        new ActualizarTipoIdentificacionFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de identificación se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarTipoIdentificacionFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de identificación se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<TipoIdentificacionDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarTipoIdentificacionPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Tipo de identificación consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<TipoIdentificacionDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosTiposIdentificacionFachadaImpl().ejecutar(new TipoIdentificacionDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Tipos de identificación consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
