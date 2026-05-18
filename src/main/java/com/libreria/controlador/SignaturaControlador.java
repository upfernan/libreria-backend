package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.SignaturaDTO;
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

@RestController
@RequestMapping("/api/v1/signaturas")
public class SignaturaControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> agregar(@RequestBody SignaturaDTO datos) {
        new AgregarSignaturaFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("La signatura se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody SignaturaDTO datos) {
        var datosConId = new SignaturaDTO.Builder()
                .id(id)
                .pasillo(datos.getPasillo())
                .estante(datos.getEstante())
                .posicion(datos.getPosicion())
                .build();
        new ActualizarSignaturaFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("La signatura se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarSignaturaFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("La signatura se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<SignaturaDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarSignaturaPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Signatura consultada exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<SignaturaDTO>>> consultarTodas() {
        var resultado = new ConsultarTodasSignaturasFachadaImpl().ejecutar(new SignaturaDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Signaturas consultadas exitosamente.", resultado), HttpStatus.OK);
    }
}
