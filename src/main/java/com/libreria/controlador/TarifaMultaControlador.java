package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.negocio.fachada.tarifamulta.impl.AgregarTarifaMultaFachadaImpl;
import com.libreria.negocio.fachada.tarifamulta.impl.ActualizarTarifaMultaFachadaImpl;
import com.libreria.negocio.fachada.tarifamulta.impl.RetirarTarifaMultaFachadaImpl;
import com.libreria.negocio.fachada.tarifamulta.impl.ConsultarTarifaMultaPorIdFachadaImpl;
import com.libreria.negocio.fachada.tarifamulta.impl.ConsultarTodasTarifasMultaFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tarifas-multa")
public class TarifaMultaControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody TarifaMultaDTO datos) {
        new AgregarTarifaMultaFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("La tarifa de multa se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody TarifaMultaDTO datos) {
        var datosConId = new TarifaMultaDTO.Builder()
                .id(id)
                .valorDiario(datos.getValorDiario())
                .fechaInicioVigencia(datos.getFechaInicioVigencia())
                .fechaFinVigencia(datos.getFechaFinVigencia())
                .build();
        new ActualizarTarifaMultaFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("La tarifa de multa se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarTarifaMultaFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("La tarifa de multa se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<TarifaMultaDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarTarifaMultaPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Tarifa de multa consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<TarifaMultaDTO>>> consultarTodas() {
        var resultado = new ConsultarTodasTarifasMultaFachadaImpl().ejecutar(new TarifaMultaDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Tarifas de multa consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
