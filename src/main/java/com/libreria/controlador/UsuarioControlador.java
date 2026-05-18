package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.UsuarioDTO;
import com.libreria.negocio.fachada.usuario.impl.RegistrarUsuarioFachadaImpl;
import com.libreria.negocio.fachada.usuario.impl.ActualizarUsuarioFachadaImpl;
import com.libreria.negocio.fachada.usuario.impl.RetirarUsuarioFachadaImpl;
import com.libreria.negocio.fachada.usuario.impl.ConsultarUsuarioPorIdFachadaImpl;
import com.libreria.negocio.fachada.usuario.impl.ConsultarTodosUsuariosFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioControlador {

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody UsuarioDTO datos) {
        new RegistrarUsuarioFachadaImpl().ejecutar(datos);
        return new ResponseEntity<>(RespuestaExito.crear("El usuario se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody UsuarioDTO datos) {
        var datosConId = new UsuarioDTO.Builder()
                .id(id)
                .tipoIdentificacion(datos.getTipoIdentificacion())
                .numeroIdentificacion(datos.getNumeroIdentificacion())
                .primerNombre(datos.getPrimerNombre())
                .segundoNombre(datos.getSegundoNombre())
                .primerApellido(datos.getPrimerApellido())
                .segundoApellido(datos.getSegundoApellido())
                .correoElectronico(datos.getCorreoElectronico())
                .build();
        new ActualizarUsuarioFachadaImpl().ejecutar(datosConId);
        return new ResponseEntity<>(RespuestaExito.crear("El usuario se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        new RetirarUsuarioFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("El usuario se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<UsuarioDTO>> consultarPorId(@PathVariable UUID id) {
        var resultado = new ConsultarUsuarioPorIdFachadaImpl().ejecutar(id);
        return new ResponseEntity<>(RespuestaExito.crear("Usuario consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<UsuarioDTO>>> consultarTodos() {
        var resultado = new ConsultarTodosUsuariosFachadaImpl().ejecutar(new UsuarioDTO.Builder().build());
        return new ResponseEntity<>(RespuestaExito.crear("Usuarios consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
