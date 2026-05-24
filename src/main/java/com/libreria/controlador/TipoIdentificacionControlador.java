package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.TipoIdentificacionDTO;
import com.libreria.negocio.fachada.tipoidentificacion.AgregarTipoIdentificacionFachada;
import com.libreria.negocio.fachada.tipoidentificacion.ActualizarTipoIdentificacionFachada;
import com.libreria.negocio.fachada.tipoidentificacion.RetirarTipoIdentificacionFachada;
import com.libreria.negocio.fachada.tipoidentificacion.ConsultarTipoIdentificacionPorIdFachada;
import com.libreria.negocio.fachada.tipoidentificacion.ConsultarTodosTiposIdentificacionFachada;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/tipos-identificacion")
public class TipoIdentificacionControlador {

    private static final Logger logger = LoggerFactory.getLogger(TipoIdentificacionControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody TipoIdentificacionDTO datos) {
        logger.info("Iniciando agregar tipoIdentificacion.");
        AgregarTipoIdentificacionFachada fachada = new AgregarTipoIdentificacionFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("El tipo de identificación se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de identificación se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody TipoIdentificacionDTO datos) {
        logger.info("Iniciando actualizacion de tipoIdentificacion con id: {}", id);
        var datosConId = new TipoIdentificacionDTO.Builder()
                .id(id)
                .nombre(datos.getNombre())
                .descripcion(datos.getDescripcion())
                .build();
        ActualizarTipoIdentificacionFachada fachada = new ActualizarTipoIdentificacionFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("El tipo de identificación se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de identificación se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de tipoIdentificacion con id: {}", id);
        RetirarTipoIdentificacionFachada fachada = new RetirarTipoIdentificacionFachadaImpl();
        fachada.ejecutar(id);
        logger.info("El tipo de identificación se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El tipo de identificación se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<TipoIdentificacionDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando tipoIdentificacion por id: {}", id);
        ConsultarTipoIdentificacionPorIdFachada fachada = new ConsultarTipoIdentificacionPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Tipo de identificación consultado exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Tipo de identificación consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<TipoIdentificacionDTO>>> consultarTodos() {
        logger.info("Consultando todos los tiposIdentificacion.");
        ConsultarTodosTiposIdentificacionFachada fachada = new ConsultarTodosTiposIdentificacionFachadaImpl();
        var resultado = fachada.ejecutar(new TipoIdentificacionDTO.Builder().build());
        logger.info("Tipos de identificación consultados exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Tipos de identificación consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
