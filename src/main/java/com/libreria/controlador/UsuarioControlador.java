package com.libreria.controlador;

import com.libreria.controlador.respuesta.RespuestaExito;
import com.libreria.dto.UsuarioDTO;
import com.libreria.negocio.fachada.usuario.RegistrarUsuarioFachada;
import com.libreria.negocio.fachada.usuario.ActualizarUsuarioFachada;
import com.libreria.negocio.fachada.usuario.RetirarUsuarioFachada;
import com.libreria.negocio.fachada.usuario.ConsultarUsuarioPorIdFachada;
import com.libreria.negocio.fachada.usuario.ConsultarTodosUsuariosFachada;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @PostMapping
    public ResponseEntity<RespuestaExito<String>> registrar(@RequestBody UsuarioDTO datos) {
        logger.info("Iniciando registro de usuario.");
        RegistrarUsuarioFachada fachada = new RegistrarUsuarioFachadaImpl();
        fachada.ejecutar(datos);
        logger.info("El usuario se registró exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El usuario se registró exitosamente.", ""), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> actualizar(@PathVariable UUID id, @RequestBody UsuarioDTO datos) {
        logger.info("Iniciando actualizacion de usuario con id: {}", id);
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
        ActualizarUsuarioFachada fachada = new ActualizarUsuarioFachadaImpl();
        fachada.ejecutar(datosConId);
        logger.info("El usuario se actualizó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El usuario se actualizó exitosamente.", ""), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExito<String>> retirar(@PathVariable UUID id) {
        logger.info("Iniciando retiro de usuario con id: {}", id);
        RetirarUsuarioFachada fachada = new RetirarUsuarioFachadaImpl();
        fachada.ejecutar(id);
        logger.info("El usuario se eliminó exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("El usuario se eliminó exitosamente.", ""), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExito<UsuarioDTO>> consultarPorId(@PathVariable UUID id) {
        logger.info("Consultando usuario por id: {}", id);
        ConsultarUsuarioPorIdFachada fachada = new ConsultarUsuarioPorIdFachadaImpl();
        var resultado = fachada.ejecutar(id);
        logger.info("Usuario consultado exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Usuario consultado exitosamente.", resultado), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RespuestaExito<List<UsuarioDTO>>> consultarTodos() {
        logger.info("Consultando todos los usuarios.");
        ConsultarTodosUsuariosFachada fachada = new ConsultarTodosUsuariosFachadaImpl();
        var resultado = fachada.ejecutar(new UsuarioDTO.Builder().build());
        logger.info("Usuarios consultados exitosamente.");
        return new ResponseEntity<>(RespuestaExito.crear("Usuarios consultados exitosamente.", resultado), HttpStatus.OK);
    }
}
