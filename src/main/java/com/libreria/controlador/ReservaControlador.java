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
        logger.info("Entre al metodo registrar del controlador...");
        RegistrarReservaFachada fachada = new RegistrarReservaFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("Sali del metodo registrar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se registró exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> eliminar(@PathVariable UUID id) {
        logger.info("Entre al metodo eliminar del controlador...");
        RetirarReservaFachada fachada = new RetirarReservaFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo eliminar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<RespuestaExito<String>> cancelar(@PathVariable UUID id) {
        logger.info("Entre al metodo cancelar del controlador...");
        CancelarReservaFachada fachada = new CancelarReservaFachadaImpl();
        fachada.ejecutar(id);
        logger.info("Sali del metodo cancelar del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se canceló exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<ReservaDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Entre al metodo consultarPorId del controlador...");
        ConsultarReservaPorIdFachada fachada = new ConsultarReservaPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Sali del metodo consultarPorId del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Reserva consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<ReservaDTO>>> consultarTodas() {
        logger.info("Entre al metodo consultarTodas del controlador...");
        ConsultarTodasReservasFachada fachada = new ConsultarTodasReservasFachadaImpl();
        var resultado = fachada.ejecutar(new ReservaDTO.Builder().build());
        logger.info("Sali del metodo consultarTodas del controlador exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Reservas consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
