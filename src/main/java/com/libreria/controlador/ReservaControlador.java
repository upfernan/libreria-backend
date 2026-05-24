package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.ReservaDTO;
import com.libreria.negocio.fachada.reserva.RegistrarReservaFachada;
import com.libreria.negocio.fachada.reserva.CancelarReservaFachada;
import com.libreria.negocio.fachada.reserva.RetirarReservaFachada;
import com.libreria.negocio.fachada.reserva.ConsultarReservaPorIdFachada;
import com.libreria.negocio.fachada.reserva.ConsultarTodasReservasFachada;
import com.libreria.negocio.fachada.reserva.impl.RegistrarReservaFachadaImpl;
import com.libreria.negocio.fachada.reserva.impl.CancelarReservaFachadaImpl;
import com.libreria.negocio.fachada.reserva.impl.RetirarReservaFachadaImpl;
import com.libreria.negocio.fachada.reserva.impl.ConsultarReservaPorIdFachadaImpl;
import com.libreria.negocio.fachada.reserva.impl.ConsultarTodasReservasFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaControlador {

    private static final Logger logger = LoggerFactory.getLogger(ReservaControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody ReservaDTO datos) {
        logger.info("Iniciando registro de reserva.");
        RegistrarReservaFachada fachada = new RegistrarReservaFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("La reserva se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se registró exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> eliminar(@PathVariable UUID id) {
        logger.info("Iniciando eliminacion de reserva con id: {}", id);
        RetirarReservaFachada fachada = new RetirarReservaFachadaImpl();
        fachada.ejecutar(id);
        logger.info("La reserva se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<RespuestaExito<String>> cancelar(@PathVariable UUID id) {
        logger.info("Iniciando cancelacion de reserva con id: {}", id);
        CancelarReservaFachada fachada = new CancelarReservaFachadaImpl();
        fachada.ejecutar(id);
        logger.info("La reserva se canceló exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se canceló exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<ReservaDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando reserva por id: {}", id);
        ConsultarReservaPorIdFachada fachada = new ConsultarReservaPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Reserva consultada exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Reserva consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<ReservaDTO>>> consultarTodas() {
        logger.info("Consultando todas las reservas.");
        ConsultarTodasReservasFachada fachada = new ConsultarTodasReservasFachadaImpl();
        var resultado = fachada.ejecutar(new ReservaDTO.Builder().build());
        logger.info("Reservas consultadas exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Reservas consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
