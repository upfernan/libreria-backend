package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.PrestamoDTO;
import com.libreria.negocio.fachada.prestamo.RegistrarPrestamoFachada;
import com.libreria.negocio.fachada.prestamo.ActualizarPrestamoFachada;
import com.libreria.negocio.fachada.prestamo.RetirarPrestamoFachada;
import com.libreria.negocio.fachada.prestamo.CerrarPrestamoFachada;
import com.libreria.negocio.fachada.prestamo.ConsultarPrestamoPorIdFachada;
import com.libreria.negocio.fachada.prestamo.ConsultarTodosPrestamosFachada;
import com.libreria.negocio.fachada.prestamo.impl.RegistrarPrestamoFachadaImpl;
import com.libreria.negocio.fachada.prestamo.impl.ActualizarPrestamoFachadaImpl;
import com.libreria.negocio.fachada.prestamo.impl.RetirarPrestamoFachadaImpl;
import com.libreria.negocio.fachada.prestamo.impl.CerrarPrestamoFachadaImpl;
import com.libreria.negocio.fachada.prestamo.impl.ConsultarPrestamoPorIdFachadaImpl;
import com.libreria.negocio.fachada.prestamo.impl.ConsultarTodosPrestamosFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoControlador {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody PrestamoDTO datos) {
        logger.info("Entre al metodo registrar del controlador...");
        RegistrarPrestamoFachada fachada = new RegistrarPrestamoFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("Sali del metodo registrar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El préstamo se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<PrestamoDTO>> actualizar(@PathVariable UUID id, @RequestBody PrestamoDTO datos) {
        logger.info("Entre al metodo actualizar del controlador...");
        var datosConId = new PrestamoDTO.Builder()
                .id(id)
                .fechaDevolucionEsperada(datos.getFechaDevolucionEsperada())
                .estadoPrestamo(datos.getEstadoPrestamo())
                .build();
        ActualizarPrestamoFachada fachada = new ActualizarPrestamoFachadaImpl();
        var resultado = fachada.ejecutar(datosConId);
        logger.info("Sali del metodo actualizar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El préstamo se actualizó exitosamente.", resultado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Entre al metodo retirar del controlador...");
        RetirarPrestamoFachada fachada = new RetirarPrestamoFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo retirar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El préstamo se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<RespuestaExito<String>> cerrar(@PathVariable UUID id) {
        logger.info("Entre al metodo cerrar del controlador...");
        CerrarPrestamoFachada fachada = new CerrarPrestamoFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo cerrar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El préstamo se cerró exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<PrestamoDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Entre al metodo consultarPorId del controlador...");
        ConsultarPrestamoPorIdFachada fachada = new ConsultarPrestamoPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Sali del metodo consultarPorId del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Préstamo consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<PrestamoDTO>>> consultarTodos() {
        logger.info("Entre al metodo consultarTodos del controlador...");
        ConsultarTodosPrestamosFachada fachada = new ConsultarTodosPrestamosFachadaImpl();
        var resultado = fachada.ejecutar(new PrestamoDTO.Builder().build());
        logger.info("Sali del metodo consultarTodos del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Préstamos consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
