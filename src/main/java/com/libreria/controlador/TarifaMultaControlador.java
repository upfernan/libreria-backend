package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.TarifaMultaDTO;
import com.libreria.negocio.fachada.tarifamulta.AgregarTarifaMultaFachada;
import com.libreria.negocio.fachada.tarifamulta.ActualizarTarifaMultaFachada;
import com.libreria.negocio.fachada.tarifamulta.RetirarTarifaMultaFachada;
import com.libreria.negocio.fachada.tarifamulta.ConsultarTarifaMultaPorIdFachada;
import com.libreria.negocio.fachada.tarifamulta.ConsultarTodasTarifasMultaFachada;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/tarifas-multa")
public class TarifaMultaControlador {

    private static final Logger logger = LoggerFactory.getLogger(TarifaMultaControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody TarifaMultaDTO datos) {
        logger.info("Iniciando agregar tarifaMulta.");
        AgregarTarifaMultaFachada fachada = new AgregarTarifaMultaFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("La tarifa de multa se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La tarifa de multa se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody TarifaMultaDTO datos) {
        logger.info("Iniciando actualizacion de tarifaMulta con id: {}", id);
        var datosConId = new TarifaMultaDTO.Builder()
                .id(id)
                .valorDiario(datos.getValorDiario())
                .fechaInicioVigencia(datos.getFechaInicioVigencia())
                .fechaFinVigencia(datos.getFechaFinVigencia())
                .build();
        ActualizarTarifaMultaFachada fachada = new ActualizarTarifaMultaFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("La tarifa de multa se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La tarifa de multa se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de tarifaMulta con id: {}", id);
        RetirarTarifaMultaFachada fachada = new RetirarTarifaMultaFachadaImpl();
        fachada.ejecutar(id);
        logger.info("La tarifa de multa se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La tarifa de multa se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<TarifaMultaDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando tarifaMulta por id: {}", id);
        ConsultarTarifaMultaPorIdFachada fachada = new ConsultarTarifaMultaPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Tarifa de multa consultada exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Tarifa de multa consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<TarifaMultaDTO>>> consultarTodas() {
        logger.info("Consultando todas las tarifasMulta.");
        ConsultarTodasTarifasMultaFachada fachada = new ConsultarTodasTarifasMultaFachadaImpl();
        var resultado = fachada.ejecutar(new TarifaMultaDTO.Builder().build());
        logger.info("Tarifas de multa consultadas exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Tarifas de multa consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
