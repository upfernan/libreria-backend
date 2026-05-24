package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.SignaturaDTO;
import com.libreria.negocio.fachada.signatura.AgregarSignaturaFachada;
import com.libreria.negocio.fachada.signatura.ActualizarSignaturaFachada;
import com.libreria.negocio.fachada.signatura.RetirarSignaturaFachada;
import com.libreria.negocio.fachada.signatura.ConsultarSignaturaPorIdFachada;
import com.libreria.negocio.fachada.signatura.ConsultarTodasSignaturasFachada;
import com.libreria.negocio.fachada.signatura.impl.AgregarSignaturaFachadaImpl;
import com.libreria.negocio.fachada.signatura.impl.ActualizarSignaturaFachadaImpl;
import com.libreria.negocio.fachada.signatura.impl.RetirarSignaturaFachadaImpl;
import com.libreria.negocio.fachada.signatura.impl.ConsultarSignaturaPorIdFachadaImpl;
import com.libreria.negocio.fachada.signatura.impl.ConsultarTodasSignaturasFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/signaturas")
public class SignaturaControlador {

    private static final Logger logger = LoggerFactory.getLogger(SignaturaControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody SignaturaDTO datos) {
        logger.info("Iniciando agregar signatura.");
        AgregarSignaturaFachada fachada = new AgregarSignaturaFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("La signatura se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La signatura se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody SignaturaDTO datos) {
        logger.info("Iniciando actualizacion de signatura con id: {}", id);
        var datosConId = new SignaturaDTO.Builder()
                .id(id)
                .pasillo(datos.getPasillo())
                .estante(datos.getEstante())
                .posicion(datos.getPosicion())
                .build();
        ActualizarSignaturaFachada fachada = new ActualizarSignaturaFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("La signatura se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La signatura se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de signatura con id: {}", id);
        RetirarSignaturaFachada fachada = new RetirarSignaturaFachadaImpl();
        fachada.ejecutar(id);
        logger.info("La signatura se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("La signatura se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<SignaturaDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando signatura por id: {}", id);
        ConsultarSignaturaPorIdFachada fachada = new ConsultarSignaturaPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Signatura consultada exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Signatura consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<SignaturaDTO>>> consultarTodas() {
        logger.info("Consultando todas las signaturas.");
        ConsultarTodasSignaturasFachada fachada = new ConsultarTodasSignaturasFachadaImpl();
        var resultado = fachada.ejecutar(new SignaturaDTO.Builder().build());
        logger.info("Signaturas consultadas exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Signaturas consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
