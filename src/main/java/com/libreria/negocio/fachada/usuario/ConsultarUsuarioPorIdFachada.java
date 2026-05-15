package com.libreria.negocio.fachada.usuario;

import com.libreria.dto.UsuarioDTO;
import com.libreria.negocio.fachada.FachadaConRetorno;
import java.util.UUID;

public interface ConsultarUsuarioPorIdFachada extends FachadaConRetorno<UUID, UsuarioDTO> {

}
