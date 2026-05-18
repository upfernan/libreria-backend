package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.ReservaDTO;
import com.libreria.negocio.fachada.reserva.impl.RegistrarReservaFachadaImpl;
import com.libreria.negocio.fachada.reserva.impl.CancelarReservaFachadaImpl;
import com.libreria.negocio.fachada.reserva.impl.AtenderReservaFachadaImpl;
import com.libreria.negocio.fachada.reserva.impl.ConsultarReservaPorIdFachadaImpl;
import com.libreria.negocio.fachada.reserva.impl.ConsultarTodasReservasFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody ReservaDTO datos) {
        new RegistrarReservaFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se registró exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> cancelar(@PathVariable UUID id) {
        new CancelarReservaFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se canceló exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}/atender")
    public ResponseEntity<RespuestaExito<String>> atender(@PathVariable UUID id) {
        new AtenderReservaFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("La reserva se atendió exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<ReservaDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarReservaPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Reserva consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<ReservaDTO>>> consultarTodas() {
        var resultado = new ConsultarTodasReservasFachadaImpl().ejecutar(new ReservaDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Reservas consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
